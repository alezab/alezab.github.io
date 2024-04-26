# JAVA

## Temat 1. Klasa i obiekt

Zadanie 1.
Zdefiniuj klasę Point posiadającą dwa publiczne, ostateczne pola x, y. Napisz konstruktor ustawiający te wartości.
Zdefiniuj klasę Segment reprezentującą odcinek, posiadającą dwa prywatne punkty klasy Point. Wygeneruj akcesory i mutatory klasy Segment. Napisz publiczną metodę, która zwraca długość odcinka. W kolejnym kroku usuń mutatory i utwórz konstruktor ustawiający te pola na wartości swoich dwóch parametrów.

https://github.com/kdmitruk/java_lab_2024/commit/6dae17f7c1b4fa68676c39cde6697ce2c263a62d

main

```java
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
         Point point;
         point=new Point(2.4, 5.5);
         System.out.println(point);

         Segment seg= new Segment(point, new Point(6.7, 8.5));

        System.out.println(seg.length());
    }
}
```

point

```java
public class Point {
    public final double x,y;

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
```

segment

```java
public class Segment {
    private Point start;
    private Point end;

    public Segment(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
    public double length(){
        return Math.hypot(end.x -start.x, end.y- start.y);
    }

}
```

Zadanie 2.
Zdefiniuj w klasie Segment publiczną metodę toSvg(), która zwróci napis zawierający kod języka znacznikowego SVG pozwalający wyświetlić tę linię.

https://github.com/kdmitruk/java_lab_2024/commit/5bf2e2ddbb2003ef6025a06c8a88f8a385fdb706

main

```java
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
         Point point;
         point=new Point(2.4, 5.5);
         System.out.println(point);

         Segment seg= new Segment(point, new Point(6.7, 8.5));

//        System.out.println(seg.length());
        System.out.println(seg.toSvg());

    }
}
```

segment

```java
import java.util.Locale;

public class Segment {
    private Point start;
    private Point end;

    public Segment(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
    public String toSvg(){
        return String.format(Locale.ENGLISH,"<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" style=\"stroke:red;stroke-width:2\" />", start.x,start.y, end.x,end.y);
    }
    //<line x1="0" y1="0" x2="300" y2="200" style="stroke:red;stroke-width:2" />


    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
    public double length(){
        return Math.hypot(end.x -start.x, end.y- start.y);
    }

}
```

Zadanie 3.
Napisz funkcję (metodę klasy głównej), która przyjmie: obiekt segment klasy Segment oraz obiekt point klasy Point. Funkcja powinna zwrócić odcinek prostopadły do segment, rozpoczynający się w punkcie point o długości równej odcinkowi segment. Następnie zmodyfikuj tę metodę tak, aby zwracała tablicę dwóch możliwych do konstrukcji linii oraz przenieś tę metodę jako statyczną do klasy Segment. Szczególne przypadki należy zignorować.

https://github.com/kdmitruk/java_lab_2024/commit/c250508eb3c56baad95d5f6bab9771ffcadc7849

main

```java
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
         Point point;
         point=new Point(20, 50);
//         System.out.println(point);

         Segment seg= new Segment(point, new Point(60, 40));

//        System.out.println(seg.length());
        System.out.println(seg.toSvg());

        Segment[] p_seg = Segment.perpendicularTo(seg, point);
        System.out.println(p_seg[0].toSvg());
        System.out.println(p_seg[1].toSvg());

    }
}
```

segment

```java
import java.util.Locale;

public class Segment {
    private Point start;
    private Point end;

    public Segment(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
    public String toSvg(){
        return String.format(Locale.ENGLISH,"<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" style=\"stroke:red;stroke-width:2\" />", start.x,start.y, end.x,end.y);
    }
    //<line x1="0" y1="0" x2="300" y2="200" style="stroke:red;stroke-width:2" />


    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
    public double length(){
        return Math.hypot(end.x -start.x, end.y- start.y);
    }

    public static Segment[] perpendicularTo(Segment s, Point p) {
        double dx = s.end.x - s.start.x;
        double dy = s.end.y - s.start.y;

        return new Segment[] {
                new Segment(p, new Point(
                        p.x - dy, p.y + dx
                )),
                new Segment(p, new Point(
                        p.x + dy, p.y - dx
                )),
        };
    }

}
```

Zadanie 4.
Zdefiniuj klasę Polygon posiadającą prywatną tablicę punktów. Konstruktor tej klasy powinien przyjmować tablicę punktów. Napisz publiczną metodę toSvg() działającą analogicznie jak w poprzednim zadaniu.

https://github.com/kdmitruk/java_lab_2024/commit/f9f0a321e563c134e6693664ef4cd923a5fcf3de

main

```java
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
      /*   Point point;
         point=new Point(20, 50);
//         System.out.println(point);

         Segment seg= new Segment(point, new Point(60, 40));

//        System.out.println(seg.length());
        System.out.println(seg.toSvg());

        Segment[] p_seg = Segment.perpendicularTo(seg, point);
        System.out.println(p_seg[0].toSvg());
        System.out.println(p_seg[1].toSvg()); */
        Polygon poly = new Polygon(new Point[]{
                new Point (30, 70),
                new Point (60, 80),
                new Point (50, 40)
        });
        System.out.println(poly.toSvg());

    }
}
```

polygon

```java
import java.util.Locale;

public class Polygon {
    private Point[] points;

    public Polygon(Point[] points) {
        this.points = points;
    }
    public String toSvg (){
        String result = "";
        for(int i=0; i<this.points.length; i++)
        {
            result += String.format(Locale.ENGLISH, "%f,%f " , points[i].x, points[i].y );
        }
        return String.format(Locale.ENGLISH,"<polygon points=\"%s\" style=\"fill:lime;stroke:purple;stroke-width:3\" />", result);
    }
}
//<polygon points="200,10 250,190 150,190" style="fill:lime;stroke:purple;stroke-width:3" />
```

Zadanie 5.

W klasie Polygon napisz konstruktor kopiujący, wykonujący głęboką kopię obiektu.

## Temat 2. Paradygmaty programowania obiektowego

Zadanie 1.
Zdefiniuj klasę Style o finalnych, publicznych polach klasy String: fillColor, strokeColor oraz Double: strokeWidth. Napisz trójargumentowy konstruktor ustawiający te wartości. Napisz publiczną metodę toSvg() zwracającą napis, będący opcją style, którą można umieścić np. w tagu \<polygon\>.

https://github.com/kdmitruk/java_lab_2024/commit/f2982dcede6a55fb1ec3db95f036117e934048d3

main

```java
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
      /*   Point point;
         point=new Point(20, 50);
//         System.out.println(point);

         Segment seg= new Segment(point, new Point(60, 40));

//        System.out.println(seg.length());
        System.out.println(seg.toSvg());

        Segment[] p_seg = Segment.perpendicularTo(seg, point);
        System.out.println(p_seg[0].toSvg());
        System.out.println(p_seg[1].toSvg()); */
        Polygon poly = new Polygon(new Point[]{
                new Point (30, 70),
                new Point (60, 80),
                new Point (50, 40)
        });
        Style style= new Style("pink","black",6.0);
        Polygon polygon= new Polygon(new Point[]{
                new Point (500, 100),
                new Point (220, 20),
                new Point (400, 25),
                new Point (70, 33),
        },style);
        System.out.println(polygon.toSvg());
        System.out.println(poly.toSvg());

    }
}
```

polygon

```java
import java.util.Locale;

public class Polygon {
    private Point[] points;
    private Style style;

    public Polygon(Point[] points) {
        this.points = points;
        this.style = new Style();
 }
    public Polygon(Point[] points, Style style) {
        this.points = points;
        this.style = style;
    }

    public String toSvg (){
        String result = "";
        for(int i=0; i<this.points.length; i++)
        {
            result += String.format(Locale.ENGLISH, "%f,%f " , points[i].x, points[i].y );
        }
        return String.format(Locale.ENGLISH,"<polygon points=\"%s\" style=\"%s />", result,style.toSvg());
    }
}
```

style

```java
import java.util.Locale;

public class Style {
    public final String fillColor;
    public final String strokeColor;
    public final double strokeWidth;

    public Style(String fillColor, String strokeColor, double strokeWidth) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
    }

    public Style() {
        this("transparent","black",1.0);
    }

    public String toSvg(){
        return String.format(Locale.ENGLISH,
                "fill:%s;stroke:%s;stroke-width:%f\" />",
                fillColor,strokeColor,strokeWidth);
    }

}
```

Zmodyfikuj klasę Polygon dodając do jej konstruktora argument Style i modyfikując jej metodę toSvg(), aby uwzględniała styl. Dopuść możliwość pominięcia stylu przy konstrukcji. Wówczas należy narysować przezroczysty (fillColor) wielokąt, z czarnym obrysem (strokeColor) o grubości jednego piksela (strokeWidth).

Zadanie 2.
Napisz klasę SvgScene posiadającą prywatną listę obiektów Polygon. Napisz metodę, która przyjmuje obiekt klasy Polygon oraz dodaje go do listy w obiekcie SvgScene. Napisz funkcję save(String), która utworzy plik HTML w ścieżce danej argumentem i zapisze do niego reprezentacje wszystkich wielokątów znajdujących się na kanwie.

https://github.com/kdmitruk/java_lab_2024/commit/37e5e50ebe010f0d42ca9dc33bb481a840225a3d

svg/src/Main.java

```java
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
      /*   Point point;
         point=new Point(20, 50);
//         System.out.println(point);

         Segment seg= new Segment(point, new Point(60, 40));

//        System.out.println(seg.length());
        System.out.println(seg.toSvg());

        Segment[] p_seg = Segment.perpendicularTo(seg, point);
        System.out.println(p_seg[0].toSvg());
        System.out.println(p_seg[1].toSvg()); */
        Polygon poly = new Polygon(new Point[]{
                new Point(30, 70),
                new Point(60, 80),
                new Point(50, 40)
        });
        Style style = new Style("pink", "black", 6.0);
        Polygon polygon = new Polygon(new Point[]{
                new Point(500, 100),
                new Point(220, 20),
                new Point(400, 25),
                new Point(70, 33),
        }, style);
        System.out.println(polygon.toSvg());
        System.out.println(poly.toSvg());
        Scene scene = new Scene();
        scene.add(polygon);
        scene.add(poly);
        scene.save("/tmp/out.html");
    }
}
```

```java
import java.util.Locale;

public class Polygon {
    private Point[] points;
    private Style style;

    public Polygon(Point[] points) {
        this.points = points;
        this.style = new Style();
 }
    public Polygon(Point[] points, Style style) {
        this.points = points;
        this.style = style;
    }

    public Point getBound() {
        double x = 0, y = 0;
        for (int i = 0; i < points.length; i++) {
            x = Math.max(x, points[i].x);
            y = Math.max(y, points[i].y);
        }
        return new Point(x, y);
    }

    public String toSvg (){
        String result = "";
        for(int i=0; i<this.points.length; i++)
        {
            result += String.format(Locale.ENGLISH, "%f,%f " , points[i].x, points[i].y );
        }
        return String.format(Locale.ENGLISH,
                "<polygon points=\"%s\" style=\"%s />", result,style.toSvg());
    }
}
```

```java
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Scene {
    private ArrayList<Polygon> shapes = new ArrayList<>();
    public void add(Polygon polygon)
    {
        shapes.add(polygon);
    }
    public Point getBounds() {
        double x = 0, y = 0;
        for (Point p : shapes
                .stream()
                .map(Polygon::getBound)
                .toList()) {
            x = Math.max(x, p.x);
            y = Math.max(y, p.y);
        }
        return new Point(x, y);
    }
    public void save(String path)
    {
        try {
            FileWriter fileWriter = new FileWriter(path);
            Point bounds = getBounds();
            fileWriter.write("<HTML>");
            fileWriter.write("<body>");
            fileWriter.write(
                    String.format(
                            "<svg width=\"%f\" height=\"%f\">\n",
                            bounds.x,
                            bounds.y
                    )
            );
            for(Polygon polygon : shapes)
                fileWriter.write("\t" + polygon.toSvg() + "\n");
            fileWriter.write("</svg>");
            fileWriter.write("</body>");
            fileWriter.write("</HTML>");
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("can't write to "+ path);
        }
    }

}
```

Zadanie 3.
Napisz publiczną, statyczną metodę wytwórczą klasy Polygon o nazwie square. Funkcja powinna przyjąć jako argumenty: obiekt Line, obiekt Style i zwrócić wielokąt będący kwadratem, którego przekątną jest dany odcinek.

Przeciąż metodę klasy Line zwracającą prostopadły odcinek tak, aby przyjmowała jako dodatkowy argument długość zwracanego odcinka.

Zadanie 4a.
Utwórz klasę abstrakcyjną Shape, która otrzyma jako pole, obiekt klasy Style. Uczyń pole tego obiektu chronionym. Utwórz publiczny konstruktor, który ustawia to pole. Napisz abstrakcyjną metodę toSvg(). Zmodyfikuj klasę Polygon, aby dziedziczyła po klasie Shape.

https://github.com/kdmitruk/java_lab_2024/commit/a17838afae845b521b0c4e007316e6cd05347365

```java
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
      /*   Point point;
         point=new Point(20, 50);
//         System.out.println(point);

         Segment seg= new Segment(point, new Point(60, 40));

//        System.out.println(seg.length());
        System.out.println(seg.toSvg());

        Segment[] p_seg = Segment.perpendicularTo(seg, point);
        System.out.println(p_seg[0].toSvg());
        System.out.println(p_seg[1].toSvg()); */
        Polygon poly = new Polygon(new Point[]{
                new Point(30, 70),
                new Point(60, 80),
                new Point(50, 40)
        });
        Style style = new Style("pink", "black", 6.0);
        Polygon polygon = new Polygon(new Point[]{
                new Point(500, 100),
                new Point(220, 20),
                new Point(400, 25),
                new Point(70, 33),
        }, style);
        System.out.println(polygon.toSvg());
        System.out.println(poly.toSvg());
        Scene scene = new Scene();
        Ellipse ellipse = new Ellipse(style, new Point(100, 200), 50.5, 75.7);
        scene.add(polygon);
        scene.add(poly);
        scene.add(ellipse);
        scene.save("/tmp/out.html");
    }
}
```

```java
import java.util.Locale;

public class Polygon extends Shape{
    private Point[] points;


    public Polygon(Point[] points) {
        this.points = points;

 }
    public Polygon(Point[] points, Style style) {
        super(style);
        this.points = points;

    }
    @Override
    public Point getBound() {
        double x = 0, y = 0;
        for (int i = 0; i < points.length; i++) {
            x = Math.max(x, points[i].x);
            y = Math.max(y, points[i].y);
        }
        return new Point(x, y);
    }
    @Override
    public String toSvg (){
        String result = "";
        for(int i=0; i<this.points.length; i++)
        {
            result += String.format(Locale.ENGLISH, "%f,%f " , points[i].x, points[i].y );
        }
        return String.format(Locale.ENGLISH,
                "<polygon points=\"%s\" style=\"%s />", result,style.toSvg());
    }
}
```

```java
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Scene {
    private ArrayList<Shape> shapes = new ArrayList<>();
    public void add(Shape polygon)
    {
        shapes.add(polygon);
    }
    public Point getBounds() {
        double x = 0, y = 0;
        for (Point p : shapes
                .stream()
                .map(Shape::getBound)
                .toList()) {
            x = Math.max(x, p.x);
            y = Math.max(y, p.y);
        }
        return new Point(x, y);
    }
    public void save(String path)
    {
        try {
            FileWriter fileWriter = new FileWriter(path);
            Point bounds = getBounds();
            fileWriter.write("<HTML>");
            fileWriter.write("<body>");
            fileWriter.write(
                    String.format(
                            "<svg width=\"%f\" height=\"%f\">\n",
                            bounds.x,
                            bounds.y
                    )
            );
            for(Shape polygon : shapes)
                fileWriter.write("\t" + polygon.toSvg() + "\n");
            fileWriter.write("</svg>");
            fileWriter.write("</body>");
            fileWriter.write("</HTML>");
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("can't write to "+ path);
        }
    }

}
```

Zadanie 4b.
Napisz klasę Ellipse dziedziczącą po Shape, posiadającą prywatne pola: środek elipsy (Point), długości promieni i styl. W jej implementacji metody toSvg() powinno znaleźć się rysowanie z użyciem tagu \<ellipse\>.

https://github.com/kdmitruk/java_lab_2024/commit/07c3c5907859ff781ebdce04fcde267f8e1cc23d

```java
import java.util.Locale;

public class Ellipse extends Shape{
    private Point center;
    private double rx, ry;
    @Override
    public Point getBound() {
        return new Point(center.x + rx + style.strokeWidth, center.y + ry + style.strokeWidth);
    }

    @Override
    public String toSvg() {
        return String.format(Locale.ENGLISH,"<ellipse rx=\"%f\" ry=\"%f\" cx=\"%f\" cy=\"%f\"\n" +
                "  style=\"%s\" />",rx,ry,center.x,center.y, style.toSvg());
    }

    public Ellipse(Style style, Point center, double rx, double ry) {
        super(style);
        this.center = center;
        this.rx = rx;
        this.ry = ry;
    }
}
```

Zmodyfikuj klasę SvgScene, aby posiadała tablicę obiektów klasy Shape i korzystając z polimorfizmu zapisz w niej obiekty typu Polygon i Ellipse.

## Temat 3. Polimorfizm. Elementy inżynierii oprogramowania

Zadanie 0.

Z klasy Shape usuń wszystkie pola i uczyń ją interfejsem. Przemianuj klasę Point na Vec2.

Zadanie 1.

Napisz klasę SolidFilledPolygon dziedziczącą po Polygon. Klasa powinna posiadać prywatne pole String color ustawiane, obok tablicy punktów, w konstruktorze. Przemodeluj funkcję toSvg w interfejsie Shape tak, aby możliwe było przekazanie jej parametru typu String, który zostanie umieszczony w tagu rysowanego obiektu. Wykorzystaj poniższy kod:
"<polygon points=\"%s\" %s />"
W klasie SolidFilledPolygon zdefiniuj metodę toSvg, która nadpisze metodę klasy nadrzędnej. Wewnątrz tej metody wywołaj metodę toSvg klasy nadrzędnej, przekazując jej jako parametr napis powstały ze sformatowania:
"fill=\"%s\" %s "
kolejno kolorem i parametrem napisowym.

Zastanów się, jakie konsekwencje dla struktury programu miałoby stworzenie analogicznej klasy dziedziczącej po klasie Ellipse oraz próba dodawania innych parametrów do tagu.

```java
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SolidFilledPolygon poly = new SolidFilledPolygon(new Vec2[]{
                new Vec2(30, 70),
                new Vec2(60, 80),
                new Vec2(50, 40)
        },"green");
//        Polygon polygon = new Polygon(new Vec2[]{
//                new Vec2(500, 100),
//                new Vec2(220, 20),
//                new Vec2(400, 25),
//                new Vec2(70, 33),
//        });
//        System.out.println(polygon.toSvg());
//        System.out.println(poly.toSvg());
        Scene scene = new Scene();
//        Ellipse ellipse = new Ellipse(new Vec2(100, 200), 50.5, 75.7);
        scene.add(poly);
//        scene.add(poly);
//        scene.add(ellipse);
        scene.save("/tmp/out.html");
    }
}
```

```java
import java.util.Locale;

public class Polygon extends Shape{
    private Vec2[] points;


    public Polygon(Vec2[] points) {
        this.points = points;
    }


    @Override
    public Vec2 getBound() {
        double x = 0, y = 0;
        for (int i = 0; i < points.length; i++) {
            x = Math.max(x, points[i].x);
            y = Math.max(y, points[i].y);
        }
        return new Vec2(x, y);
    }

    @Override
    public String toSvg() {
        return this.toSvg("");
    }


    public String toSvg (String parameters){
        String result = "";
        for(int i=0; i<this.points.length; i++)
        {
            result += String.format(Locale.ENGLISH, "%f,%f " , points[i].x, points[i].y );
        }
        return String.format(Locale.ENGLISH,
                "<polygon points=\"%s\" %s/>", result,parameters);
    }
}
```

```java
public class SolidFilledPolygon extends Polygon {
    private String fillColour;

    @Override
    public String toSvg(String parameters) {
        String f=String.format("fill=\"%s\" %s ",fillColour,parameters);
        return super.toSvg(f);
    }

    public SolidFilledPolygon(Vec2[] points, String colour){
        super(points);
        fillColour=colour;
    }
}
```

Zadanie 2.

Zdefiniuj klasę ShapeDecorator implementującą interfejs Shape, która posiadać będzie chronione pole Shape decoratedShape. Pole to powinno być ustawiane w konstruktorze. Nadpisz metodę toSvg w taki sposób, by zawierała wywołanie tej samej metody na rzecz obiektu decoratedShape.

Po klasie ShapeDecorator podziedzicz nową klasę SolidFillShapeDecorator. Klasa ta powinna posiadać prywatne pole String color. W konstruktorze ma przyjmować obiekt klasy Shape oraz kolor wypełnienia typu String. W jej metodzie toSvg wywołaj metodę toSvg na rzecz decoratedShape, z parametrami jak w zadaniu 1.

Utwórz dwa obiekty klasy SolidFillShapeDecorator tak, aby parametrem jednego był obiekt wielokąta, a drugiego elipsy.

```java
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        /*SolidFilledPolygon poly = new SolidFilledPolygon(new Vec2[]{
                new Vec2(30, 70),
                new Vec2(60, 80),
                new Vec2(50, 40)
        },"green");
//        Polygon polygon = new Polygon(new Vec2[]{
//                new Vec2(500, 100),
//                new Vec2(220, 20),
//                new Vec2(400, 25),
//                new Vec2(70, 33),
//        });
//        System.out.println(polygon.toSvg());
//        System.out.println(poly.toSvg());
        Scene scene = new Scene();
//        Ellipse ellipse = new Ellipse(new Vec2(100, 200), 50.5, 75.7);
        scene.add(poly);
//        scene.add(poly);
//        scene.add(ellipse);

        scene.save("/tmp/out.html");

         */
        Shape polygon = new Polygon(new Vec2[]{
                new Vec2(500, 100),
                new Vec2(220, 20),
                new Vec2(400, 25),
                new Vec2(70, 33),
        });
        polygon = new SolidFillShapeDecorator(polygon, "red");

        Shape ellipse = new Ellipse(new Vec2(100, 200), 50.5, 75.7);
        ellipse = new SolidFillShapeDecorator(ellipse,"blue");

        Scene scene = new Scene();
        scene.add(polygon);
        scene.add(ellipse);

        scene.save("/tmp/out.html");



    }
}
```

```java
import java.util.Locale;

public class Ellipse implements Shape{
    private Vec2 center;
    private double rx, ry;
    @Override
    public Vec2 getBound() {
        return new Vec2(center.x + rx, center.y + ry);
    }

    @Override
    public String toSvg(String param) {
        return String.format(Locale.ENGLISH,"<ellipse rx=\"%f\" ry=\"%f\" cx=\"%f\" cy=\"%f\" %s/>",
                rx,ry,center.x,center.y,param);
    }
    /*public String toSvg(String param){
        //TODO :potem

    }*/
    public Ellipse(Vec2 center, double rx, double ry) {
        this.center = center;
        this.rx = rx;
        this.ry = ry;
    }
}
```

```java
public interface Shape {
      Vec2 getBound();
      String toSvg(String param);

}
```

```java
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Scene {
    private ArrayList<Shape> shapes = new ArrayList<>();
    public void add(Shape polygon)
    {
        shapes.add(polygon);
    }
    public Vec2 getBounds() {
        double x = 0, y = 0;
        for (Vec2 p : shapes
                .stream()
                .map(Shape::getBound)
                .toList()) {
            x = Math.max(x, p.x);
            y = Math.max(y, p.y);
        }
        return new Vec2(x, y);
    }
    public void save(String path)
    {
        try {
            FileWriter fileWriter = new FileWriter(path);
            Vec2 bounds = getBounds();
            fileWriter.write("<HTML>");
            fileWriter.write("<body>");
            fileWriter.write(
                    String.format(
                            "<svg width=\"%f\" height=\"%f\">\n",
                            bounds.x,
                            bounds.y
                    )
            );
            for(Shape polygon : shapes)
                fileWriter.write("\t" + polygon.toSvg("") + "\n");
            fileWriter.write("</svg>");
            fileWriter.write("</body>");
            fileWriter.write("</HTML>");
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("can't write to "+ path);
        }
    }

}
```

```java
import java.util.Locale;

public class Polygon implements Shape{
    private Vec2[] points;


    public Polygon(Vec2[] points) {
        this.points = points;
    }


    @Override
    public Vec2 getBound() {
        double x = 0, y = 0;
        for (int i = 0; i < points.length; i++) {
            x = Math.max(x, points[i].x);
            y = Math.max(y, points[i].y);
        }
        return new Vec2(x, y);
    }



    public String toSvg (String parameters){
        String result = "";
        for(int i=0; i<this.points.length; i++)
        {
            result += String.format(Locale.ENGLISH, "%f,%f " , points[i].x, points[i].y );
        }
        return String.format(Locale.ENGLISH,
                "<polygon points=\"%s\" %s/>", result,parameters);
    }
}
```

```java
public class ShapeDecorator implements Shape{
    protected Shape decoratedShape;

    public ShapeDecorator(Shape decoratedShape) {
        this.decoratedShape = decoratedShape;
    }

    @Override
    public Vec2 getBound() {
        return decoratedShape.getBound();
    }

    @Override
    public String toSvg(String param) {
        return decoratedShape.toSvg(param);
    }
}
```

```java
public class SolidFillShapeDecorator extends ShapeDecorator{
    private String color;
    public SolidFillShapeDecorator(Shape decoratedShape, String color) {
        super(decoratedShape);
        this.color = color;
    }

    @Override
    public String toSvg(String paramet) {
        String f=String.format("fill=\"%s\" %s ",color, paramet);
        return decoratedShape.toSvg(f);
    }
}
```

Zadanie 3.

Utwórz klasę StrokeShapeDecorator posiadającą prywatne pola String color i double width, które powinny być ustawione w konstruktorze. Wywołaj metodę toSvg podobnie jak w zadaniu 2. formatując napis
"stroke=\"%s\" stroke-width=\"%f\" "
kolorem i grubością obrysu. Przetestuj udekorowanie tą klasą obiektów będących wynikiem poprzedniego zadania.

Zadanie 4.

Utwórz klasę TransformationDecorator odpowiadającą za wpisanie w wyświetlany tab informacji o przekształceniach afinicznych: translacji, rotacji i skalowaniu. Na potrzeby każdego z tych działań stwórz prywatne pola:

    boolean translate, Vec2 translateVector,
    boolean rotate, double rotateAngle, Vec2 rotateCenter,
    boolean scale, Vec2 scaleVector.

Wewnątrz klasy TransformationDecorator zdefiniuj publiczną klasę Builder. Zdefiniuj w niej prywatne pola, jednakowe z polami w klasie zewnętrznej oraz pole Shape shape. Wartości logiczne powinny być fałszywe. Napisz po jednej metodzie ustawiającej parametry transformacji i zmieniającej wartość logiczną na prawdziwą na znak, że transformacja ma się wykonać. Funkcje powinny zwracać obiekt klasy Builder z wprowadzonymi zmianami. Napisz w klasie Builder metodę build, która utworzy obiekt TransformationDecorator, przekazując mu jako parametr obiekt shape, a następnie ustawi wszystkim polom w tym obiekcie wartości zapisane w obiekcie Buildera i zwróci tak stworzony obiekt.

W klasie TransformationDecorator nadpisz metodę toSvg tak, aby poskładać w niej napis definiujący transformację z elementów:
"translate(%f %f) ", translateVector.x, translateVector.y
"rotate(%f %f %f) ", rotateAngle, rotateCenter.x, rotateCenter.y
"scale(%f %f) ", scaleVector.x, scaleVector.y

Umieść je w we własności “transform”:
"transform=\"%s\" %s", result, parameters.

Przetestuj tworzenie klasy TransformationDecorator za pomocą całości lub części dostępnych transformacji.

Uzyskanie możliwości zastosowania filtra oraz wypełnienia obiektu gradientem wymaga zapisania stosownego kodu w tagu <defs>, którego zawartość nie będzie wprost renderowana. Lokalne obiekty w SVG mogą być identyfikowane za pomocą unikalnych nazw (id), a odwoływać można się do nich za pomocą składni “url(#id)”.

Zadanie 5a.

W klasie SvgScene utwórz prywatne, statyczne pole SvgScene instance, początkowo równe null. Napisz akcesor do tego pola. Jeżeli znajduje się tam null, należy je zainicjalizować.

Zadanie 5b.

Dodaj do klasy SvgScene tablicę String defs[] oraz metodę dodającą elementy do tej tablicy, wzorując się na tablicy shapes i metodzie addShape. W metodzie saveHtml uwzględnij dopisanie tagów <defs> do wynikowego pliku.

Zdefiniuj klasę DropShadowDecorator dziedziczącą po ShapeDecorator. Jej zadaniem jest udekorowanie obiektu Shape rzucanym cieniem. Jest to realizowane przez umieszczenie w tagu <defs> sformatowanego kodu:

\t<filter id=\"f%d\" x=\"-100%%\" y=\"-100%%\" width=\"300%%\" height=\"300%%\">\n" +
"\t\t<feOffset result=\"offOut\" in=\"SourceAlpha\" dx=\"5\" dy=\"5\" />\n" +
"\t\t<feGaussianBlur result=\"blurOut\" in=\"offOut\" stdDeviation=\"5\" />\n" +
"\t\t<feBlend in=\"SourceGraphic\" in2=\"blurOut\" mode=\"normal\" />\n" +
"\t</filter>", index

oraz w metodzie toSvg:

"filter=\"url(#f%d)\" ", index

gdzie w obu przypadkach index jest liczbą całkowitą, unikalną dla tego filtra. Unikalność indeksu zagwarantuj przy użyciu prywatnego, statycznego pola klasy.

Zadanie 6.

Łącząc wiedzę wyniesioną z zadania 4 i 5 zdefiniuj klasę GradientFillShapeDecorator dziedziczącą po ShapeDecorator, której celem jest wypełnienie kształtu poziomym, barwnym gradientem.

Gradient wymaga umieszczenia w tagu <defs> napisu rozpoczynającego się od:
"\t<linearGradient id=\"g%d\" >\n", index
a następnie dla każdego koloru i jego położenia:
\t\t<stop offset=\"%f\" style=\"stop-color:%s\" />\n", stop.offset, stop.color,
gdzie stop.offset jest liczbą zmiennoprzecinkową z przedziału 0-1, a stop.color napisem. Definicję gradientu zamyka:
"\t</linearGradient>"

Wewnątrz klasy zdefiniuj klasę Builder. W klasie Builder stwórz metodę, która przyjmuje offset i kolor, a której wielokrotne wywołania pozwalają stworzyć tablicę tych wartości definiującą przebieg gradientu.

W metodzie toSvg klasy zewnętrznej wykorzystaj sformatowany napis:
"fill=\"url(#g%d)\" ", index

Rozważ, jakie modyfikacje należałoby poczynić w programie wynikowym, aby możliwa była rezygnacja z singletonowej postaci klasy SvgScene.
