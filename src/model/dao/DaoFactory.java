package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory { // essa classe vai ter operacoes estaticas para instanciar os Daos
	
	public static SellerDao createSellerDao() { // vai expor um metodo que retorna o tipo da interface (SellerDao);
		return new SellerDaoJDBC(); // mas internamente, vai instanciar a implementacao (SellerDaoJDBC)
	}

}
