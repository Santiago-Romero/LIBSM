/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libsm;

/**
 *
 * @author Santiago Romero
 */
import java.io.*;
public class LeerContraBD {
	int vez=0;
	public String usuario="";
	public String contra="";
	public LeerContraBD(){
		try{
                    FileReader fr= new FileReader("claveBD.txt");
                    BufferedReader br= new BufferedReader(fr);
                    String escrito ="";
			while((escrito=br.readLine())!=null){
				for(int i=0;i<escrito.length();i++){
					if(vez==0){
						usuario+=escrito.substring(i,i+1);
						if(escrito.substring(i,i+1).equals("*"))
						{
							usuario=usuario.substring(0,usuario.length()-1);
							vez++;
							}
						}
					if(vez==1){
						contra+=escrito.substring(i,i+1);
						if(escrito.substring(i,i+1).equals("-")){
							contra=contra.substring(1,contra.length()-1);
							}
						}
					}
				}
			}
		catch(Exception B){
			System.out.println(B);
		}
		vez=0;
		}
	}
