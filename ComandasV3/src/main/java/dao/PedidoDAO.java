package dao;


import dao.ObjectDBUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.persistence.TypedQuery;
import models.Pedido;


public class PedidoDAO {
    
    LocalDateTime date = LocalDateTime.now();

    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    String formattedDate = date.format(myFormatObj);

    
    public void add(Pedido p){       
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();        
    }
    
    public void update(Pedido p){       
        
        
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();     
        em.getTransaction().begin();
        TypedQuery<Pedido> query = em.createQuery(
            "UPDATE Pedido SET fecha = :fecha, cliente = :cliente, estado = :estado,"
                    + "producto = :producto WHERE idPed = :idPed", Pedido.class);
        query.setParameter("fecha",p.getFecha());
        query.setParameter("cliente",p.getCliente());
        query.setParameter("estado",p.getEstado());
        query.setParameter("producto",p.getProducto());
        query.setParameter("idPed",p.getIdPed());
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();        

    }
    
    public ArrayList<Pedido> getAll(){
        ArrayList<Pedido> salida;
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();
        TypedQuery<Pedido> q = em.createQuery("select p from Pedido p",Pedido.class);
        salida = (ArrayList<Pedido>) q.getResultList();
        em.close();
        return salida;
    }
    
    public ArrayList<Pedido> getAllToday(){
        ArrayList<Pedido> salida;
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();
        TypedQuery<Pedido> q = em.createQuery("select p from Pedido p where p.fecha =:currentDate",Pedido.class);
        q.setParameter("currentDate", LocalDate.now());
        salida = (ArrayList<Pedido>) q.getResultList();
    
  
        em.close();
        return salida;
    }
    
    public void delete(Pedido p){
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Pedido pedido = em.find(Pedido.class, p.getIdPed());
        em.remove(pedido);
        em.getTransaction().commit();
        em.close();        
        
    }
            
    
}
