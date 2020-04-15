package dao.bill;
import pojo.Bill;

import java.sql.Connection;
import java.util.List;

public interface BillDao {
    int getBillCountByProviderId(Connection connection, String providerId)throws Exception;
    int modify(Connection connection, Bill bill)throws Exception;
    Bill getBillById(Connection connection, String id)throws Exception;
    int deleteBillById(Connection connection, String delId)throws Exception;
    List<Bill> getBillList(Connection connection, Bill bill)throws Exception;
    int add(Connection connection, Bill bill)throws Exception;
}
