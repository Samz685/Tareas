package dao;


import dao.ObjectDBUtil;
import java.util.ArrayList;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import models.Producto;


public class ProductoDAO {
    
    public void add(Producto p){       
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();        
    }
    
    public void update(Producto p){       
        
        
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();     
        em.getTransaction().begin();
        TypedQuery<Producto> query = em.createQuery(
            "UPDATE Producto SET nombre = :nombre, tipo = :tipo, precio = :precio,"
                    + "disponibilidad = :disponibilidad WHERE idPro = :idPro", Producto.class);
        query.setParameter("nombre",p.getNombre());
        query.setParameter("tipo",p.getTipo());
        query.setParameter("precio",p.getPrecio());
        query.setParameter("disponibilidad",p.getDisponibilidad());
        query.setParameter("idPro",p.getIdPro());
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();        

    }
    
    public ArrayList<Producto> getAll(){
        ArrayList<Producto> salida;
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();
        TypedQuery<Producto> q = em.createQuery("select p from Producto p",Producto.class);
        salida = (ArrayList<Producto>) q.getResultList();
        em.close();
        return salida;
    }
    
    public ArrayList<Producto> getProducto(String param){
        ArrayList<Producto> salida;
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();
        TypedQuery<Producto> q = em.createQuery("select p from Producto p where p.nombre =:nombre",Producto.class);
        q.setParameter("nombre", param);
        salida = (ArrayList<Producto>) q.getResultList();
        em.close();
        return salida;
    }
    
    public void delete(Producto p){
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Producto producto = em.find(Producto.class, p.getIdPro());
        em.remove(producto);
        em.getTransaction().commit();
        em.close();        
        
    }
            
    
}
