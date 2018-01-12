package com.dber.base.util;

import com.dber.util.CipherUtil;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* CipherUtil Tester. 
* 
* @author <Authors name> 
* @since <pre>һ�� 10, 2018</pre> 
* @version 1.0 
*/ 
public class CipherUtilTest { 

@Before
public void before() {
} 

@After
public void after() {
} 

/** 
* 
* Method: SHAEncode(String data) 
* 
*/ 
@Test
public void testSHAEncode() {
    System.out.println(CipherUtil.SHAEncode("sdafasfsadaf"));
    System.out.println(CipherUtil.SHAEncode("111"));
    System.out.println(CipherUtil.SHAEncode("111"));
    System.out.println(CipherUtil.SHAEncode("111").length());
}


/** 
* 
* Method: encryptEncode(String algorithm, String data) 
* 
*/ 
@Test
public void testEncryptEncode() {
//TODO: Test goes here... 
/* 
try { 
   Method method = CipherUtil.getClass().getMethod("encryptEncode", String.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: byteArrayToHexStr(byte byteArray[]) 
* 
*/ 
@Test
public void testByteArrayToHexStr() {
//TODO: Test goes here... 
/* 
try { 
   Method method = CipherUtil.getClass().getMethod("byteArrayToHexStr", byte.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 
