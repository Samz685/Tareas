package dao;


import dao.ObjectDBUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import models.ProductoData;



public class ProductoDataDAO {
    
    public ArrayList<ProductoData> traerVentasHoy() {
        ArrayList<ProductoData> listaVentas = new ArrayList();
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();
        TypedQuery<Object[]> q = em.createQuery("SELECT pr.nombre as nProducto, SUM(pr.precio) as suma\n"
                + "FROM Producto pr\n"
                + "JOIN pr.pedidos p\n"
                + "WHERE p.fecha =:current_date GROUP BY pr.nombre", Object[].class);
        q.setParameter("current_date", LocalDate.now());
        List<Object[]> rows = q.getResultList();
        em.close();
        for (Object[] row : rows) {
            ProductoData pV = new ProductoData();
            pV.setNombre(row[0].toString());
            pV.setVenta(Integer.valueOf(row[1].toString()));
            listaVentas.add(pV);
        }
        return listaVentas;
    }
    
    public ArrayList<ProductoData> traerVentasSemana() {
        
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusWeeks(1);
        
        ArrayList<ProductoData> listaVentas = new ArrayList();
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();
        TypedQuery<Object[]> q = em.createQuery("SELECT pr.nombre as nProducto, SUM(pr.precio) as suma\n"
                + "FROM Producto pr\n"
                + "JOIN pr.pedidos p\n"
                + "WHERE p.fecha >=: weekAgo AND p.fecha <=: today GROUP BY pr.nombre", Object[].class);
        q.setParameter("today", today);
        q.setParameter("weekAgo", weekAgo);
        List<Object[]> rows = q.getResultList();
        em.close();
        for (Object[] row : rows) {
            ProductoData pV = new ProductoData();
            pV.setNombre(row[0].toString());
            pV.setVenta(Integer.valueOf(row[1].toString()));
            listaVentas.add(pV);
        }
        return listaVentas;
    }
    
    public ArrayList<ProductoData> traerVentasMes() {
        
        LocalDate today = LocalDate.now();
        LocalDate monthAgo = today.minusMonths(1);
        
        ArrayList<ProductoData> listaVentas = new ArrayList();
        var em = ObjectDBUtil.getEntityManagerFactory().createEntityManager();
        TypedQuery<Object[]> q = em.createQuery("SELECT pr.nombre as nProducto, SUM(pr.precio) as suma\n"
                + "FROM Producto pr\n"
                + "JOIN pr.pedidos p\n"
                + "WHERE p.fecha >=: monthAgo AND p.fecha <=: today GROUP BY pr.nombre", Object[].class);
        q.setParameter("today", today);
        q.setParameter("monthAgo", monthAgo);
        List<Object[]> rows = q.getResultList();
        em.close();
        for (Object[] row : rows) {
            ProductoData pV = new ProductoData();
            pV.setNombre(row[0].toString());
            pV.setVenta(Integer.valueOf(row[1].toString()));
            listaVentas.add(pV);
        }
        return listaVentas;
    }
    
    
            
    
}
