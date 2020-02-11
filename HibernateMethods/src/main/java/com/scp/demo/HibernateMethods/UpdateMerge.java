package com.scp.demo.HibernateMethods;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UpdateMerge {
	public static SessionFactory sfactory = new Configuration().
			configure("mysql.cfg.xml").buildSessionFactory();


	
	public static void main(String[] args)throws InterruptedException  {
		Employee e1 = new Employee(22,"ABCD", 25);//T -- mem -- ABCD
		Session session1 = sfactory.openSession();
		Transaction tr1 = session1.beginTransaction();
		session1.save(e1); // P //
		session1.flush();
		tr1.commit(); // MEM -- ABCD DB ABCD -- E1-P
		session1.clear();
		// 	DB ABCD -- 			E1-D [ABCD]-MEM
		e1.setEmpName("PQRS");  // DB -- ABCD -- MEM-PQRS[D] -- S1--NO
		
		
		
		Session session2 = sfactory.openSession();
		Transaction tr2 = session2.beginTransaction();
		Employee dbemp = session2.get(Employee.class, 1);//P
		//DB -- ABCD   S2-P-- ABCD  --- 
		dbemp.setEmpName("XXXX"); /// mem -- PQRS DB -- ABCD -- s2 -- XXXX
		
		session2.merge(e1); //s2 --- [xxxx[p],pqrs[d-p]] db -- abcd
		session2.flush();
		tr2.commit();
		
	}

}
