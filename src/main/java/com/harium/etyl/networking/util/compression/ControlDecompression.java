package com.harium.etyl.networking.util.compression;

public class ControlDecompression {

	public static void main(String[] args) {
		
		//Comando enviado
		//byte b = -127;
		
		//int command = b+128;
		int command = 101;
		
		System.out.println("/k "+command);
		
		int KEYBOARD_SIZE = 8;
		//VirtualKeyboard
		boolean[] keyboard = new boolean[KEYBOARD_SIZE];
		
		for(int k=0;k<KEYBOARD_SIZE;k++){
			keyboard[k] = false;
		}
		
		int MAX_INT = 256;
		
		for(int k=2, i=KEYBOARD_SIZE-1;i>=0;i--){
			
			if(command>=MAX_INT/k){
			
				keyboard[i] = true;
				
				command -= MAX_INT/k;
				
			}else{
				System.out.println("command = "+command+"<"+MAX_INT/k);
			}

			k*=2;
			
		}
		
		for(int i=0;i<KEYBOARD_SIZE;i++){
			if(keyboard[i]){
				System.out.println(i+" pressionado!");
			}
		}
		
	}

}
