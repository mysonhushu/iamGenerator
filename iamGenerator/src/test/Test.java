/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.iamgenerator.config.SystemLoad;
import com.iamgenerator.dao.GeneratorDao;

/**
 *
 * @author kado
 */
public class Test {
    public static void main(String args[])
    {
//        getAllTableAndColumnsInfo
        SystemLoad.init();
        GeneratorDao.getInstance().getAllTableAndColumnsInfo();
    }
        
}
