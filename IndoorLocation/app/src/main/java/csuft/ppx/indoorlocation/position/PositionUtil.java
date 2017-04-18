package csuft.ppx.indoorlocation.position;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csuft.ppx.indoorlocation.beacon.Beacon;

/**
 * Created by hwm on 2017/4/10.
 */

//定位工具类
public class PositionUtil {
    private static Map<String,Point> BeaconPoints=new HashMap<>();
    //定义接受到的beacon转化为的圆的arraylist
    private List<Circular> circulars=new ArrayList<>();
    //定义当前的位置
    private Point point;

    private static volatile PositionUtil positionUtil;
    //初始化
    public static PositionUtil getIstance(){
        if(positionUtil==null){
            positionUtil=new PositionUtil();
        }
        return positionUtil;
    }

    private PositionUtil() {
        // TODO Auto-generated constructor stub
        PointInital();
    }

    public Point Position(List<Beacon> beacons){
        //每次调用就清空arraylist
        circulars.clear();
        //把给出的beacon转化为圆加入到集合中
        for(Beacon b:beacons){
            circulars.add(toCircular(b));
        }
        //根据3个圆定位
        point=getPoint(circulars);
        return point;
    }

    //根据建模，把每个beacon分别赋予一个坐标点
    private void PointInital(){
       // BeaconPoints.put("19:18:FC:03:B6:AD",new Point(5,5));
        BeaconPoints.put("19:18:FC:03:B6:A0",new Point(2.4,4));
        BeaconPoints.put("19:18:FC:03:B6:AA",new Point(0,0));
        //BeaconPoints.put("19:18:FC:03:B6:AB",new Point(2.4,12));
        BeaconPoints.put("19:18:FC:03:B6:C2",new Point(0,8));
        //BeaconPoints.put("19:18:FC:03:B6:BA",new Point(5,5));
       BeaconPoints.put("19:18:FC:03:07:D2",new Point(2.4,12.03));
       // BeaconPoints.put("19:18:FC:03:07:AD",new Point(5,5));
       // BeaconPoints.put("19:18:FC:03:07:A4",new Point(5,5));
    }
    //根据给定的Beacon，把他转化为相对应的圆
    private static Circular toCircular(Beacon beacon){
        Circular c=null;
        int txPower=beacon.measuredPower;
        double rssi=beacon.rssi;
        double X=-1,Y=-1;
        //获取半径
        double radius=calculateAccuracy(txPower, rssi);
        //根据，uuId来获取圆心
        String MAC=beacon.mac;
        //根据MAC来获取当前Beacon的坐标的
        X=BeaconPoints.get(MAC).getX();
        Y=BeaconPoints.get(MAC).getY();
        if(X==-1||Y==-1){
            System.out.print("出错");
            return null;
        }
        //建立一个Beacon 的圆模型
        System.out.println("mac："+MAC+"     X："+X+"   Y:"+Y+"   txPower:"+txPower+"    rssi:"+rssi+"    R:"+radius);
        c=new Circular(radius, X, Y);
        return c;
    }


    //根据给出的三个圆来确定位置的大概位置
    private static Point getPoint(List<Circular> circulars){

        Point result=new Point();
        result.setX(-1);
        result.setY(-1);

        //根据三个圆得到的三个交点，人的位置就在这三个点围成的三角形内
        Point p1=intersect(circulars.get(0),circulars.get(1),circulars.get(2));
        Point p2=intersect(circulars.get(0),circulars.get(2),circulars.get(1));
        Point p3=intersect(circulars.get(1),circulars.get(2),circulars.get(0));

        /*
        //根据权值来合理计算出位置坐标点
        double sum=circulars.get(0).getR()+circulars.get(1).getR()+circulars.get(2).getR();

        //三个圆每个圆半径所占的比例
        double ps1=circulars.get(0).getR()/sum;
        double ps2=circulars.get(1).getR()/sum;
        double ps3=circulars.get(2).getR()/sum;

        //设置返回的结果坐标点
        result.setX((p1.getX()*ps1+p2.getX()*ps2+p3.getX()*ps3)/3);
        result.setY((p1.getY()*ps1+p2.getY()*ps2+p3.getY()*ps3)/3);
        if(result.getX()<0||result.getY()<0)
            System.out.print("位置定位失败！重新定位");
            */
        result.setX((p1.getX()+p2.getX()+p3.getX())/3);
        result.setY((p1.getY()+p2.getY()+p3.getY())/3);
        return result;
    }



    //求两个圆c1和c2的交点,并且这个交点在c3这个圆的范围内
    private  static Point intersect(Circular c1,Circular c2,Circular c3){
        System.out.println("半径为"+c1.getR()+"与 半径为"+c2.getR()+"的交点");
        System.out.println();
        Point result;
        double x1=c1.getX(),y1=c1.getY(),r1=c1.getR();
        double x2=c2.getX(),y2=c2.getY(),r2=c2.getR();
        double x3=c3.getX(),y3=c3.getY(),r3=c3.getR();
        if(y1!=y2){
            //两个圆不一起在x轴上
            double M,N,A,B;
            //圆c1的方程表达式为  x*x+y*y-2*x1*x-2*y1*y+x1*x1+y1*y1-r1*r1=0,圆c2的方程表达式一样
            //两圆相交，可以得直线方程为  A-Bx=y,求得 A 和 B;
            A=pf(x1)-pf(x2)+pf(y1)-pf(y2)+pf(r2)-pf(r1);
            A=A/(2*y1-2*y2);

            B=(x1-x2)/(y1-y2);
           // System.out.println("A :"+A+"   B："+B);

            M=(x1+(A-y1)*B)/(1+pf(B));
            N=(pf(x1)+pf(A-y1)-pf(r1))/(1+pf(B));

           // System.out.println("M :"+M+"  N:"+N);

            //判断圆的交点有几个
            double index=pf(2*M)-4*N;

            if(index<0){
                //两个圆没有交点
                System.out.println("两个圆没有交点,取他们半径中间值为");
                //如果两个圆没有交点就取他们半径的中间值
                result=new Point();
                result.setX((c1.getX()+c2.getX())/2);
                result.setY((c1.getY()+c2.getY())/2);
                System.out.println("合理交点为:("+result.getX()+","+result.getY()+")");
                //return result;
            }
            else{
                //两个圆有交点
                double xa=M+Math.sqrt(pf(M)-N);
                double ya=A-B*xa;
               // System.out.println("一组交点为 ("+xa+","+ya+")");
                double xb=M-Math.sqrt(pf(M)-N);
                double yb=A-B*xb;
                //System.out.println("另一组交点为 ("+xb+","+yb+")");

                if(xa==xb){
                    //只有一个交点
                    result=new Point(xa, ya);
                }else{
                    //两个交点，判断与第三个圆圆心距离更近的那个点
                    if((pf(xa-x3)+pf(ya-y3))<=(pf(xb-x3)+pf(yb-y3))){
                        result=new Point(xa, ya);
                    }else{
                        result=new Point(xb, yb);
                    }

                }
                System.out.println("合理交点为:("+result.getX()+","+result.getY()+")");
                // return result;
            }
        }else{
            //两个圆都在x轴上
            //如果两圆心的距离大于半径之和，不存在交点
            if(Math.abs(x1-x2)>(r1+r2))
                return null;
            //存在交点
            double xx=(x1+x2)/2;

            double yy1=Math.sqrt(pf(r1)-pf(xx-x1));
            double yy2=-Math.sqrt(pf(r1)-pf(xx-x1));

            //判断哪个点距离c3圆心的距离更小
            if(pf(yy1-y3)<=pf(yy2-y3)){
                result=new Point(xx, yy1);
            }else{
                result=new Point(xx, yy2);
            }
            System.out.println("合理的点为("+result.getX()+","+result.getY()+")");
            //return result;
        }
        return result;
    }

    //求一个数的平方
    private static double pf(double x){
        return x*x;
    }


    //根据txPower和Rssi来估算距离
    private  static double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            return accuracy;
        }
    }


}
