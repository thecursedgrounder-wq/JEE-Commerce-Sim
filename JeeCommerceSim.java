import java.util.*;
class JCS{
 static Scanner s=new Scanner(System.in);
 static List<U> us=new ArrayList<>();static List<I> is=new ArrayList<>();static int nu,ni;
 static class U{String id="U"+ ++nu,p,h,r;List<String>b=new ArrayList<>();int f;boolean l;
  U(String p,String x,String r){this.p=p;h=Integer.toString(x.hashCode());this.r=r;}
  boolean ok(String x){if(l)return false;if(h.equals(Integer.toString(x.hashCode()))){f=0;return true;}f++;if(f>2)l=true;return false;}}
 static class I{String id="I"+ ++ni,n,c,sp,im="img";double p;int st;I(String n,double p,String c,String sp){this.n=n;this.p=p;this.c=c;this.sp=sp;}}
 static{U a=new U("admin","admin123","A"),s1=new U("seller1","sell123","S"),s2=new U("seller2","sell123","S"),b1=new U("buyer1","buy123","B"),b2=new U("buyer2","buy123","B");us.add(a);us.add(s1);us.add(s2);us.add(b1);us.add(b2);I i1=new I("Laptop",1299.99,"Electronics","seller1"),i2=new I("Mouse",29.99,"Electronics","seller1"),i3=new I("Shoes",89.99,"Sports","seller2"),i4=new I("JavaBook",45.5,"Books","seller2"),i5=new I("Coffeemaker",65,"Home","seller1");i1.st=i2.st=i3.st=i4.st=i5.st=1;is.add(i1);is.add(i2);is.add(i3);is.add(i4);is.add(i5);}
 static U fU(String p){for(U u:us)if(u.p.equalsIgnoreCase(p))return u;return null;}
 static I fI(String id){for(I i:is)if(i.id.equals(id))return i;return null;}
 static List<I> ok(){List<I> r=new ArrayList<>();for(I i:is)if(i.st==1)r.add(i);return r;}
 static List<I> f(String k,String c){List<I> r=new ArrayList<>();for(I i:ok())if((k==null||i.n.toLowerCase().contains(k))&&(c==null||i.c.equalsIgnoreCase(c)))r.add(i);return r;}
 static List<I> fs(U u){List<I> r=new ArrayList<>();for(I i:is)if(i.sp.equals(u.p))r.add(i);return r;}
 static void sh(List<I> x){for(I i:x)System.out.println(i.id+" "+i.n+" $"+i.p+" ["+i.sp+"]"+(i.st==0?" P":i.st==2?" R":""));}
 static String C(){String[] c={"Electronics","Clothing","Books","Home","Sports","Toys","Food","Other"};for(int j=0;j<c.length;j++)System.out.print(j+1+"."+c[j]+" ");System.out.print("type:");int k=in();return k<1||k>c.length?null:c[k-1];}
 public static void main(String[]a){for(;;){System.out.println("\n1 Login 2 Reg 3 Guest 0 Exit");switch(in()){case 1:U u=lg();if(u!=null)rt(u);break;case 2:rg();break;case 3:gs();break;case 0:return;}}}
 static U lg(){System.out.print("pseudo:");String p=s.nextLine();U u=fU(p);if(u==null){System.out.println("nf");return null;}System.out.print("pass:");return u.ok(s.nextLine())?u:null;}
 static void rg(){System.out.print("pseudo:");String p=s.nextLine();System.out.print("pass:");String x=s.nextLine();System.out.print("1 Buyer 2 Seller:");U u=new U(p,x,in()==2?"S":"B");if(fU(p)!=null||p.length()<3||x.length()<4)System.out.println("taken/short");else{us.add(u);System.out.println("ID "+u.id);rt(u);}}
 static void rt(U u){if(u.r.equals("A"))ad(u);else if(u.r.equals("S"))sl(u);else by(u);}
 static void bu(U u){System.out.print("id:");I i=fI(s.nextLine().trim());if(i==null)System.out.println("nf");else if(i.st!=1)System.out.println("na");else{System.out.print("confirm y: ");if(s.nextLine().equalsIgnoreCase("y")){System.out.println("bought "+i.n);if(u!=null)u.b.add(i.id+" "+i.n);}}}
 static void gs(){for(;;){System.out.println("\n1 view 2 search 3 type 4 buy 0 back");switch(in()){case 1:sh(ok());break;case 2:System.out.print("kw:");sh(f(s.nextLine().toLowerCase(),null));break;case 3:sh(f(null,C()));break;case 4:bu(null);break;case 0:return;}}}
 static void by(U u){for(;;){System.out.println("\n1 view 2 search 3 type 4 buy 5 hist 0 out");switch(in()){case 1:sh(ok());break;case 2:System.out.print("kw:");sh(f(s.nextLine().toLowerCase(),null));break;case 3:sh(f(null,C()));break;case 4:bu(u);break;case 5:for(String x:u.b)System.out.println(x);break;case 0:return;}}}
 static void sl(U u){for(;;){System.out.println("\n1 add 2 mine 3 del 0 out");switch(in()){case 1:System.out.print("name:");String n=s.nextLine().trim();System.out.print("price:");double p=rd();String c=C();System.out.print("img:");String im=s.nextLine().trim();I i=new I(n,p,c==null?"Other":c,u.p);i.im=im.isEmpty()?"img":im;is.add(i);System.out.println(i.id+" P");break;case 2:sh(fs(u));break;case 3:System.out.print("id:");I d=fI(s.nextLine().trim());if(d==null)System.out.println("nf");else if(!d.sp.equals(u.p))System.out.println("not yours");else{is.remove(d);System.out.println("del");}break;case 0:return;}}}
 static void ad(U u){for(;;){System.out.println("\n1 pend 2 app 3 rej 4 del 5 all 6 users 0 out");switch(in()){case 1:List<I>p=new ArrayList<>();for(I i:is)if(i.st==0)p.add(i);sh(p);break;case 2:System.out.print("id:");I a=fI(s.nextLine().trim());if(a!=null){a.st=1;System.out.println("approved");}break;case 3:System.out.print("id:");I r=fI(s.nextLine().trim());if(r!=null){r.st=2;System.out.println("rejected");}break;case 4:System.out.print("id:");I d=fI(s.nextLine().trim());if(d!=null){is.remove(d);System.out.println("deleted");}break;case 5:sh(is);break;case 6:for(U x:us)System.out.println(x.r+" "+x.p+" "+x.id+(x.l?" L":""));break;case 0:return;}}}
 static int in(){try{return Integer.parseInt(s.nextLine().trim());}catch(Exception e){return-1;}}
 static double rd(){try{return Double.parseDouble(s.nextLine().trim());}catch(Exception e){return-1;}}
}