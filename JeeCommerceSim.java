import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class JeeCommerceSim {
    static Scanner sc = new Scanner(System.in);

    // ---------- MODELS ----------
    static class User {
        String id = UUID.randomUUID().toString().substring(0, 8), pseudo, role;
        String passHash;
        int fails;
        boolean locked;

        User(String pseudo, String pass, String role) {
            this.pseudo = pseudo; this.passHash = hash(pass); this.role = role;
        }

        boolean ok(String p) {
            if (locked) { System.out.println("LOCKED"); return false; }
            if (passHash.equals(hash(p))) { fails = 0; return true; }
            if (++fails >= 3) { locked = true; System.out.println("LOCKED (3 fails)"); }
            return false;
        }
    }
    static String hash(String p) { return Integer.toString(p.hashCode()); }

    static class Buyer extends User { ArrayList<String> bought = new ArrayList<>(); Buyer(String p, String w) { super(p, w, "BUYER"); } }
    static class Seller extends User { ArrayList<String> mine = new ArrayList<>(); Seller(String p, String w) { super(p, w, "SELLER"); } }
    static class Admin extends User { Admin(String p, String w) { super(p, w, "ADMIN"); } }

    static class Item {
        static int n = 1;
        String id = "I" + (n++);
        String name, cat, img, sellerId, sellerPseudo;
        double price;
        int status = 0; // 0 pending, 1 approved, 2 rejected
        Item(String name, double price, String cat, String img, String sid, String sp) {
            this.name = name; this.price = price; this.cat = cat; this.img = img;
            this.sellerId = sid; this.sellerPseudo = sp;
        }
    }

    // ---------- SERVICES ----------
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Item> items = new ArrayList<>();

    static {
        users.add(new Admin("admin", "admin123"));
        Seller s1 = new Seller("seller1", "sell123");
        Seller s2 = new Seller("seller2", "sell123");
        users.add(s1); users.add(s2);
        users.add(new Buyer("buyer1", "buy123"));
        users.add(new Buyer("buyer2", "buy123"));
        items.add(pre(s1, "Laptop Dell", 1299.99, "Electronics"));
        items.add(pre(s1, "Wireless Mouse", 29.99, "Electronics"));
        items.add(pre(s2, "Running Shoes", 89.99, "Sports"));
        items.add(pre(s2, "Java Book", 45.50, "Books"));
        items.add(pre(s1, "Coffee Maker", 65.00, "Home"));
        for (Item i : items) i.status = 1;
    }
    static Item pre(Seller s, String name, double p, String c) { return new Item(name, p, c, "img/" + name + ".jpg", s.id, s.pseudo); }

    static User findUser(String p) { for (User u : users) if (u.pseudo.equalsIgnoreCase(p)) return u; return null; }
    static Item findItem(String id) { for (Item i : items) if (i.id.equals(id)) return i; return null; }
    static ArrayList<Item> approved() { ArrayList<Item> r = new ArrayList<>(); for (Item i : items) if (i.status == 1) r.add(i); return r; }
    static ArrayList<Item> pending() { ArrayList<Item> r = new ArrayList<>(); for (Item i : items) if (i.status == 0) r.add(i); return r; }
    static ArrayList<Item> bySeller(String id) { ArrayList<Item> r = new ArrayList<>(); for (Item i : items) if (i.sellerId.equals(id)) r.add(i); return r; }
    static ArrayList<Item> filt(String kw, String cat) {
        ArrayList<Item> r = new ArrayList<>();
        for (Item i : approved())
            if ((kw == null || i.name.toLowerCase().contains(kw.toLowerCase())) && (cat == null || cat.equals("all") || i.cat.equalsIgnoreCase(cat)))
                r.add(i);
        return r;
    }
    static void show(ArrayList<Item> r) { if (r.isEmpty()) { System.out.println("none"); return; } for (Item i : r) System.out.println(i.id + " " + i.name + " $" + i.price + " [" + i.sellerPseudo + "] " + (i.status == 0 ? "PENDING" : i.status == 1 ? "" : "REJECTED")); }

    // ---------- UI ----------
    public static void main(String[] a) {
        while (true) {
            System.out.println("\n1 Login  2 Register  3 Guest   0 Exit");
            switch (readInt()) {
                case 1: User u = login(); if (u != null) route(u); break;
                case 2: register(); break;
                case 3: guest(); break;
                case 0: return;
            }
        }
    }

    static User login() { System.out.print("pseudo: "); String p = sc.nextLine(); System.out.print("pass: "); String w = sc.nextLine(); return (findUser(p) != null && findUser(p).ok(w)) ? findUser(p) : null; }
    static void register() { System.out.print("pseudo: "); String p = sc.nextLine(); System.out.print("pass: "); String w = sc.nextLine(); System.out.print("1 Buyer  2 Seller: "); boolean s = readInt() == 2; if (findUser(p) != null || p.length() < 3 || w.length() < 4) { System.out.println("taken or too short"); return; } User u = s ? new Seller(p, w) : new Buyer(p, w); users.add(u); System.out.println("ID " + u.id); route(u); }
    static void route(User u) { if (u instanceof Admin) admin(); else if (u instanceof Seller) seller((Seller) u); else buyer((Buyer) u); }
    static void buyBuy(Buyer b, String id) { Item i = findItem(id); if (i == null) { System.out.println("not found"); return; } if (i.status != 1) { System.out.println("not available"); return; } System.out.print("confirm buy " + i.name + " $" + i.price + "? (y): "); if (sc.nextLine().equalsIgnoreCase("y")) { System.out.println("bought"); if (b != null) b.bought.add(i.id + " " + i.name); } }

    static void guest() { while (true) { System.out.println("\n1 view  2 search  3 by type  4 buy  0 back"); switch (readInt()) { case 1: show(approved()); break; case 2: System.out.print("kw: "); show(filt(sc.nextLine(), null)); break; case 3: show(filt(null, pickCat())); break; case 4: System.out.print("id: "); buyBuy(null, sc.nextLine().trim()); break; case 0: return; } } }

    static void buyer(Buyer b) { while (true) { System.out.println("\n1 view  2 search  3 by type  4 buy  5 history  0 out"); switch (readInt()) { case 1: show(approved()); break; case 2: System.out.print("kw: "); show(filt(sc.nextLine(), null)); break; case 3: show(filt(null, pickCat())); break; case 4: System.out.print("id: "); buyBuy(b, sc.nextLine().trim()); break; case 5: for (String x : b.bought) System.out.println(x); break; case 0: return; } } }

    static void seller(Seller s) { while (true) { System.out.println("\n1 add  2 mine  3 delete  0 out"); switch (readInt()) { case 1: System.out.print("name: "); String n = sc.nextLine().trim(); System.out.print("price: "); double p = readDouble(); String c = pickCat(); System.out.print("image (url/path): "); String im = sc.nextLine().trim(); if (im.isEmpty()) im = "img/default.jpg"; Item it = new Item(n, p, c, im, s.id, s.pseudo); items.add(it); s.mine.add(it.id); System.out.println(it.id + " PENDING"); break; case 2: show(bySeller(s.id)); break; case 3: System.out.print("id: "); String id = sc.nextLine().trim(); Item d = findItem(id); if (d == null) System.out.println("not found"); else if (!d.sellerId.equals(s.id)) System.out.println("not yours"); else { items.remove(d); s.mine.remove(id); System.out.println("deleted"); } break; case 0: return; } } }

    static void admin() { while (true) { System.out.println("\n1 pending  2 approve  3 reject  4 delete  5 all  6 users  0 out"); switch (readInt()) { case 1: show(pending()); break; case 2: System.out.print("id: "); String i1 = sc.nextLine().trim(); if (findItem(i1) != null) { findItem(i1).status = 1; System.out.println("approved"); } break; case 3: System.out.print("id: "); String i2 = sc.nextLine().trim(); if (findItem(i2) != null) { findItem(i2).status = 2; System.out.println("rejected"); } break; case 4: System.out.print("id: "); String i3 = sc.nextLine().trim(); if (findItem(i3) != null) { items.remove(findItem(i3)); System.out.println("deleted"); } break; case 5: show(items); break; case 6: for (User u : users) System.out.println(u.role + " " + u.pseudo + " " + u.id + (u.locked ? " LOCKED" : "")); break; case 0: return; } } }

    static String pickCat() {
        String[] c = { "Electronics", "Clothing", "Books", "Home", "Sports", "Toys", "Food", "Other" };
        for (int i = 0; i < c.length; i++) System.out.println((i + 1) + " " + c[i]);
        System.out.print("type: ");
        int k = readInt();
        return (k >= 1 && k <= c.length) ? c[k - 1] : "Other";
    }

    static int readInt() { try { return Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) { return -1; } }
    static double readDouble() { try { return Double.parseDouble(sc.nextLine().trim()); } catch (Exception e) { return -1; } }
}
