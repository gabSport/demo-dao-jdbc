package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Main {

	public static void main(String[] args) {
		
		
		
		SellerDao sellerDao = DaoFactory.createSellerDao(); // para instanciar um Dao, nao damos um new SellerDaoJDBC, simplesmente chamamos a fabrica:
		//DaoFactory.createSellerDao. Dessa forma, o programa so conhece a interface, e nao a implementacao. (injecao de dependencia)
		
		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);	
	}
}
