package application;

import java.util.Date;
import java.util.List;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Main {

	public static void main(String[] args) {
		
		
		SellerDao sellerDao = DaoFactory.createSellerDao(); // para instanciar um Dao, nao damos um new SellerDaoJDBC, simplesmente chamamos a fabrica:
		//DaoFactory.createSellerDao. Dessa forma, o programa so conhece a interface, e nao a implementacao. (injecao de dependencia)
		
		System.out.println("==== TEST 1: seller findById ====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);	
		
		System.out.println();
		System.out.println("==== TEST 2: seller findByDepartment ====");
		Department dep  = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(dep);
		for (Seller obj : list) {
			System.out.println(obj);
		}	
		
		System.out.println();
		System.out.println("==== TEST 3: seller findAll ====");
		list = sellerDao.findAll();
		for (Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println();
		System.out.println("==== TEST 4: seller insert ====");
		Seller sell = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, dep); // instanciei um novo Seller;
		sellerDao.insert(sell); // mandei inserir esse novo Seller instanciado no banco de dados;
		System.out.println("Inserted! New id = " + sell.getId());
		
		System.out.println();
		System.out.println("==== TEST 5: seller update ====");
		seller = sellerDao.findById(1); // vou carregar os dados do vendedor cujo Id = 1;
		seller.setName("Martha Waine"); // agora vou setar para modificar o nome dele para "Martha Waine"
		sellerDao.update(seller); // em seguida, salvo esse seller, atualizando os dados dele com o update;
		System.out.println("Update completed");
	}
}
