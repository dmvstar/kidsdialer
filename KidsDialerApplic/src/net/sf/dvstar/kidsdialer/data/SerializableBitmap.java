package net.sf.dvstar.kidsdialer.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import net.sf.dvstar.kidsdialer.utils.Log;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SerializableBitmap implements Serializable {

	private static final long serialVersionUID = 111696345129311948L;

	private Bitmap bitmap;
	private static final int NO_IMAGE = -1;
	
	private int bytes = -1;
	
	private byte[] imageByteArray;
	private byte[] byteTestArray;

	private String imagePath = "";

	public SerializableBitmap(Bitmap bitmap, String path){
		this.bitmap = bitmap;
		this.imagePath = path;
	}
	
	private void fillRandomTestArray() {
		for(int i=0;i<128;i++){
			byteTestArray[i] = (byte)(Math.random()*100);
		}	
	}

	private void clearRandomTestArray() {
		for(int i=0;i<128;i++){
			byteTestArray[i] = 0;
		}	
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public String getImagePath() {
		return imagePath;
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		if (bitmap != null) {
			byteTestArray = new byte[128];
			fillRandomTestArray();
			bitmap.prepareToDraw();
			//calculate how many bytes our image consists of. 
			//Use a different value than 4 if you don't use 32bit images.
			this.bytes = bitmap.getHeight() * bitmap.getWidth()*4;
			this.bytes = bitmap.getRowBytes() * bitmap.getWidth();
			//Create a new buffer
			ByteBuffer 	buffer = ByteBuffer.allocate(bytes);
			int			iBuf[] = new int[bytes];
			
			//Move the byte data to the buffer
			bitmap.copyPixelsToBuffer(buffer);
			
			bitmap.getPixels(iBuf, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
			
			imageByteArray = buffer.array(); 
			
			int countNotNull=0; 
			for (int i = 0; i < bytes; i++) {
//			    String byteString = "0x" + Integer.toHexString(imageByteArray[i] & 0xff);
//			    Log.i( "outputPixel byte " + i + " = " + byteString);
				if(imageByteArray[i]!=0)countNotNull++; 
			}			
		    Log.i( "outputPixel byte = ["+imageByteArray.length+"]["+bytes+"]" + countNotNull);countNotNull=0;
			
			for (int i = 0; i < bytes; i++) {
//			    String byteString = "0x" + Integer.toHexString(iBuf[i] & 0xff);
//			    Log.i( "outputPixel byte " + i + " = " + byteString);
				if(iBuf[i]!=0)countNotNull++; 
			}			
		    Log.i( "outputPixel byte = ["+imageByteArray.length+"]["+bytes+"]" + countNotNull);
			
			out.writeInt(bytes);
		    out.writeUTF(imagePath);
			out.write(byteTestArray);
			out.write(imageByteArray);
			out.flush();
		} else {
			out.writeInt(NO_IMAGE);
		}
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		byteTestArray = new byte[128];
		clearRandomTestArray();

	    Log.i( "inputPixel byte = available ["+in.available()+"]");
		
		this.bytes = in.readInt();
		this.imagePath = in.readUTF();
	    in.readFully(this.byteTestArray, 0, 128);

		this.imageByteArray = new byte[bytes];
		
	    in.readFully(imageByteArray, 0, bytes);
	    int count = imageByteArray.length;

		int countNotNull=0; 
		for (int i = 0; i < bytes; i++) {
			if(imageByteArray[i]!=0)countNotNull++; 
		}			
	    Log.i( "inputPixel byte = available ["+in.available()+"]["+count+"]" + countNotNull);countNotNull=0;
	    
	    BitmapFactory.Options opt = new BitmapFactory.Options();
	    opt.inPreferredConfig = Bitmap.Config.ARGB_8888;

	    this.bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, bytes, opt);
	 
	}   	
	

}
