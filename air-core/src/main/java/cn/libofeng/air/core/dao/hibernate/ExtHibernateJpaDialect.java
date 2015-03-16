package cn.libofeng.air.core.dao.hibernate;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Bofeng
 * Date: 13-8-1
 * Time: 下午10:06
 * extend Dialect to support custom iso level
 * refer web page: http://stackoverflow.com/questions/5234240/hibernatespringjpaisolation-does-not-work
 */
public class ExtHibernateJpaDialect extends HibernateJpaDialect {
    ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>();
    ThreadLocal<Integer> originalIsolation = new ThreadLocal<Integer>();

    @Override
    public Object beginTransaction(EntityManager entityManager, TransactionDefinition definition)
            throws PersistenceException, SQLException, TransactionException {

        boolean readOnly = definition.isReadOnly();
        Connection connection = this.getJdbcConnection(entityManager, readOnly).getConnection();
        connectionThreadLocal.set(connection);
        originalIsolation.set(DataSourceUtils.prepareConnectionForTransaction(connection, definition));

        entityManager.getTransaction().begin();

        return prepareTransaction(entityManager, readOnly, definition.getName());
    }

    /*

     We just have to trust that spring won't forget to call us. If they forget,
     we get a thread-local/classloader memory leak and messed up isolation
     levels. The finally blocks on line 805 and 876 of
     AbstractPlatformTransactionManager (Spring 3.2.3) seem to ensure this,
     though there is a bit of logic between there and the actual call to this
     method.

     */
    @Override
    public void cleanupTransaction(Object transactionData) {
        try {
            super.cleanupTransaction(transactionData);
            DataSourceUtils.resetConnectionAfterTransaction(connectionThreadLocal.get(), originalIsolation.get());
        } finally {
            connectionThreadLocal.remove();
            originalIsolation.remove();
        }
    }

}
