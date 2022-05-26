package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Main {

	public static void main(String[] args) {
		
		Department obj = new Department(1, "Books");
		
		Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.0, obj);
		
		SellerDao sellerDao = DaoFactory.createSellerDao(); // para instanciar um Dao, nao damos um new SellerDaoJDBC, simplesmente chamamos a fabrica:
		//DaoFactory.createSellerDao. Dessa forma, o programa so conhece a interface, e nao a implementacao. (injecao de dependencia)
		
		System.out.println(seller);	
	}
}
