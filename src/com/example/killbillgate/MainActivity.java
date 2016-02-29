package com.example.killbillgate;

import Stuff.BillGate;
import Stuff.World;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity 
{
 public World world=new World();	
 public BillGate billGate=new BillGate();
 public float[] finger_locate_in_grid=new float[2];
 public float[] finger_locate_in_pix=new float[2];
 
 public float[] how_big_de_world_in_pix=new float[2];
 public float[] world_line_locate_in_pix=new float[4];//up down left right line's pix location
 public float world_width_pix;
 public float world_height_pix;
 public float world_width_grid;
 public float world_height_grid;
 public float gridWidth;
 public float gridHeight;
 
 public float[] world_grid_limit=new float[2];
 public float gridLength;
 public float[] moveHowManyGrid=new float[2];
 
 public float emitLineYlocation_grid;
 public float emitLineYlocation_pix;
 public float distanceToMovePix_X;
 public float distanceToMovePix_Y;
 public float nextPix_X;
 public float newPix_Y;
 public float accelerationPix_X;
 public float accelerationPix_Y;
 public float accelerationGrid_X=1;
 public float accelerationGrid_Y=1;
 
 
 public TextView topLine_tv, bottomLine_tv,leftLine_tv,rightLine_tv,emitLine_tv;
 public float[] billGateOldGrid=new float[2];
 public float[] billGateNowGrid=new float[2];
 
 boolean isStop=false;
 
 
@Override
public void onWindowFocusChanged(boolean hasFocus)
    {super.onWindowFocusChanged(hasFocus);
    say("on windows focus change");	
    scaleWorld(world);
	devide_world_into_grid(world);
	setBillScaleInGrid(billGate,3000,3000);
 	setBillScaleInPix(billGate,world);
 	RelativeLayout.LayoutParams billGateLayout=(RelativeLayout.LayoutParams)billGate.avatar.getLayoutParams();
 	billGateLayout.leftMargin=(int)(world.leftLimitPix+world.width_pix/2);
 	billGateLayout.topMargin=(int)(world.topLimitPix+world.height_pix/2);
    billGate.avatar.setLayoutParams(billGateLayout);
    accelerationPix_X=accelerationGrid_X*world.gridWidth;
    accelerationPix_Y=accelerationGrid_Y*world.gridHeight;
    billGate.avatar.setOnTouchListener(new BillGateOnTouchListener());
    
    say("billGate X "+billGate.avatar.getLeft());
    say("billGate Y "+billGate.avatar.getTop());
    say("billLayout leftMargin "+ billGateLayout.leftMargin);
    say("billLayout topMargin "+ billGateLayout.topMargin);
    
 	//  setBillLocation();	
    }
@Override
protected void onCreate(Bundle savedInstanceState)
{ super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  findViewById();

} 
private void setBillScaleInPix(BillGate billGate,World world)
	{billGate.height_pix=billGate.height_grid*world.gridHeight;
	 say("billGate height in pix =" +billGate.height_grid+"*"+world.gridHeight+"="+billGate.height_pix);
     billGate.width_pix=billGate.width_grid*world.gridWidth;
	 android.view.ViewGroup.LayoutParams billAvatarLayout=billGate.avatar.getLayoutParams();
	 billAvatarLayout.height=(int)billGate.height_pix;
  	 billAvatarLayout.width=(int)billGate.width_pix;
	 billGate.avatar.setLayoutParams(billAvatarLayout); 
	}
private void setBillLocation() 
	{	//since the billGateAvatar ImageButton is set to be
		//center Horizontal in XML,it is ok to do nothing here;
	}
private void setBillScaleInGrid(BillGate billGate,float gridNum_X,float gridNum_Y) 
	{billGate.height_grid=gridNum_Y;
	 billGate.width_grid=gridNum_X;
	}
private void devide_world_into_grid(World world)
	{world.width_grid=(float) 10000.00;
//	 world_height_px/world_width_px=world_height_grid/world_width_grid
	 world.height_grid=(world.height_pix/world.width_pix)*world.width_grid;
	 world.gridWidth=world.width_pix/world.width_grid;
	 world.gridHeight=world.gridWidth;
	 say("gridLength "+world.gridWidth);
	}
private void scaleWorld(World world)
	{   int[] XY=new int[2];
		topLine_tv.getLocationOnScreen(XY);
	 	say("topline xy "+XY[0]+"  "+XY[1]);
	 	
		world.topLimitPix=XY[1]+topLine_tv.getHeight();
	 	
		say("topline heihgt in pix "+topLine_tv.getHeight());
		say("world top limmit"+world.topLimitPix);
     	
		bottomLine_tv.getLocationOnScreen(XY);
		world.bottomLimitPix=XY[1];
		say("bottomLine height in pix "+bottomLine_tv.getHeight());
		
		leftLine_tv.getLocationOnScreen(XY);
		world.leftLimitPix=XY[0]+leftLine_tv.getWidth();
		say("left Limit in Pix "+leftLine_tv.getWidth());
		
		
		rightLine_tv.getLocationOnScreen(XY);
		world.rightLimitPix=XY[0];
		say("right limit in pix "+rightLine_tv.getWidth());
		
		world.height_pix=world.bottomLimitPix-world.topLimitPix;
		world.width_pix=world.rightLimitPix-world.leftLimitPix;
		say("world height in pix " + world.height_pix);
		say("world width in pix "+ world.width_pix);
		
	}
public void  findViewById()
   {topLine_tv=(TextView)findViewById(R.id.main_topLine_tv);
    bottomLine_tv=(TextView)findViewById(R.id.main_bottomLine_tv);
    leftLine_tv=(TextView)findViewById(R.id.main_leftLine_tv);
    rightLine_tv=(TextView)findViewById(R.id.main_rightLine_tv);
//   	emitLine_tv=(TextView)findViewById(R.id.main_emitLine_tv);
    billGate.avatar=(ImageButton)findViewById(R.id.main_bgAvatar_ib);
   }
public void say(String sayContent)
{Log.i("debug",sayContent);
}

public class BillGateOnTouchListener implements OnTouchListener
{	@Override
	public boolean onTouch(View v, MotionEvent event)
    {say("on touch start"); 
	switch(event.getAction())
	{
    case MotionEvent.ACTION_DOWN:
//    	billGate.locate_in_grid_now=countBillGateGridNum(billGate);
		say("finger touch me");
    	billGate.avatar.getLocationOnScreen(billGate.locate_in_pix_old);
    	
	case MotionEvent.ACTION_UP:
		
		//calculate the speed of billGate( in Pix )
		say("finger up");
	    billGate.avatar.getLocationOnScreen(billGate.locate_in_pix_now);
		billGate.speedInPix_X=billGate.locate_in_pix_now[0]-billGate.locate_in_pix_old[0];
		say("speed X ="+ billGate.locate_in_pix_now[0]+" - "+billGate.locate_in_pix_old[0]);
		say("speed pix x "+billGate.speedInPix_X);
		
		billGate.speedInPix_Y=billGate.locate_in_pix_now[1]-billGate.locate_in_pix_old[1];
		say("speed Y="+billGate.locate_in_pix_now[1]+" - "+billGate.locate_in_pix_old[1]);
		say("speed pix y "+billGate.speedInPix_Y);

		billGate.speedInGrid_X=billGate.speedInPix_X/world.gridWidth;
		billGate.sppedInGrid_Y=billGate.speedInPix_Y/world.gridHeight;
		
		 while(!isStop)
		 {
//		 say("it is slding");
		 if(Math.abs(billGate.speedInPix_X)>=0.001*world.gridWidth)
		 calculateVectorX();
//		 if(Math.abs(billGate.speedInPix_Y)>=0.001*world.gridHeight)
		 calculateVectorY();		
		 changeBillGateMargin(distanceToMovePix_X,distanceToMovePix_Y);  
		 try
		 {Thread.sleep(10);}
		 catch(Exception e)
		 {}
		  collisionCheck();
		  isStop=checkStop();
	
		 } 
		
	case MotionEvent.ACTION_MOVE:	
	
	moveWithFinger(billGate.avatar,event.getRawX(),event.getRawY());
	}
	
		return false;
	}

 private boolean checkStop()
   {billGate.avatar.getLocationOnScreen(billGate.locate_in_pix_now);
    if((billGate.locate_in_pix_now[1]+billGate.height_pix)>=world.bottomLimitPix
    	&& Math.abs(billGate.speedInPix_X)<0.0001*world.gridWidth	&& Math.abs(billGate.speedInPix_Y)<0.0001*world.gridHeight
    	)//if the billgate is on ground ,and volocity of both horizontal and vertical <0.0001
    	return true;
    else
	 return false;
  }

private void collisionCheck()
 {billGate.avatar.getLocationOnScreen(billGate.locate_in_pix_now);
 if(billGate.locate_in_pix_now[0]<=world.leftLimitPix || 
 (billGate.locate_in_pix_now[0]+billGate.width_pix)>=world.rightLimitPix)
 {//if the collision is happend on horizontal direction
	 billGate.speedInPix_X*=-0.9;
	 accelerationPix_X*=-1;

 }
 if(billGate.locate_in_pix_now[1]<=world.topLimitPix ||
    (billGate.locate_in_pix_now[1]+billGate.height_pix)>=world.bottomLimitPix)
 {//if collision happened in vertical direction
    billGate.speedInPix_Y*=-0.9;
 }
 
 }
private void calculateVectorX() 
 {distanceToMovePix_X=billGate.speedInPix_X;
  billGate.speedInPix_X-=accelerationPix_X;
  say("speed pix x "+billGate.speedInPix_X);
 }

private void calculateVectorY() 
{distanceToMovePix_Y=billGate.speedInPix_Y;
 billGate.speedInPix_Y-=billGate.speedInPix_Y+accelerationPix_Y;
 say("speed pix y "+billGate.speedInPix_Y);
 // move the x and y
 
 }

private void changeBillGateMargin(float distanceToMovePix_X,
		float distanceToMovePix_Y)
{RelativeLayout.LayoutParams billGateParams=(RelativeLayout.LayoutParams)billGate.avatar.getLayoutParams();
 say("leftMargin="+ billGateParams.leftMargin);
 say("topMargin="+billGateParams.topMargin);
  billGateParams.leftMargin=(int)(billGateParams.leftMargin+distanceToMovePix_X);
 billGateParams.topMargin=(int)(billGateParams.topMargin+distanceToMovePix_Y);
 billGate.avatar.setLayoutParams(billGateParams);
 
}
 
}

private void moveWithFinger(ImageButton billGateAvatar, float fingerXpix, float fingerYpix)
 {say("moving with finer moving");
	 RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)billGate.avatar
		        .getLayoutParams();
	
	 billGateAvatar.getLocationOnScreen(billGate.locate_in_pix_old);
//	 billGate.locate_in_pix_old=billGate.locate_in_pix_now;	
//	 say("bill gate x y old "+ "  "+billGate.locate_in_pix_old[0]+"  "+billGate.locate_in_pix_old[1]);
	   
	 params.leftMargin = (int)(fingerXpix - billGateAvatar.getWidth() / 2);
	 params.topMargin = (int) (fingerYpix-world.topLimitPix-billGateAvatar.getHeight()/2);
		   
			 billGateAvatar.setLayoutParams(params);
		    billGateAvatar.invalidate();
		    try
		    {Thread.sleep(0);
		    }catch(Exception e ){}
		    
//		    billGateAvatar.getLocationInWindow(billGate.locate_in_pix_now);
            
//			say("bill gate x y now"+"  "+billGate.locate_in_pix_now[0]+"  "+billGate.locate_in_pix_now[1]);
 }

private void setBillGateLocation_pix(BillGate billGate)
 {
	 
 }

private float[] getFingerLocation_pix(MotionEvent event) 
 {   float[] fingerXY=new float[2];
	 fingerXY[0]=event.getRawX();
	 fingerXY[1]=event.getRawY();
	 return fingerXY;
	 
 }

private float[] countBillGateGridNum(BillGate billGate)
 {int pixXY[]=new int[2];
  float gridXY[]=new float[2];
  billGate.avatar.getLocationOnScreen(pixXY);
  float distanceToLine_X=pixXY[0]-world.rightLimitPix;
  float distanceToLine_Y=pixXY[1]-world.topLimitPix;
  gridXY[0]=(float)distanceToLine_X/world.gridWidth;
  gridXY[1]=(float)distanceToLine_Y/world.gridHeight;
  return gridXY;
  
 }
 };

 
	

