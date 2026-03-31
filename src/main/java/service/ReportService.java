package service;

import dao.OrderDAO;

public class ReportService {
    public void reportTop5() {
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.reportTop5ProductsThisMonth();
    }
}
