import processing.core.*;


import java.util.*;
import oscP5.*;
import netP5.*;
//importing the JMusic stuff
import jm.music.data.*;
import jm.JMC;
import jm.util.*;
import jm.midi.*;

import java.io.UnsupportedEncodingException;
import java.net.*;

 class XiaoQiu extends PApplet
{
  PVector loc;
  float vx=0, vy=0;
  float r;
  float baocunr;
  float angle=0;
  float cx,cy,c;
  boolean bianxiao =true;
  
 XiaoQiu(PVector location, float r) 
 {
   loc =location;
   this.r =r ;
   baocunr = r ;
   cx =map(loc.x,0,width,0,180);
   cy =map(loc.y,0,height,0,180);
   c=cx+cy;
 
}
void update(){
  angle += 0.02*noise((float)0.001*loc.x,(float)0.001*loc.y);
  vx = 2*sin(angle);
  vy = 2*cos(angle);
  loc.x+=vx;
  loc.y+=vy;
  if(bianxiao) r -=0.3;
  if(r<=0) bianxiao=false;
  if(!bianxiao) r+=0.3;
  if(r>=baocunr) bianxiao = true;
  c+=5;
  
}
void display(){

  fill(c%360,100,100);
  ellipse(loc.x,loc.y,r,r);
  
}
void check(){
  
  if(loc.x<0) loc.x=width;
  if(loc.x>width) loc.x=0;
  if(loc.y<0) loc.y=height;
  if(loc.y>height) loc.y=0;
}
}