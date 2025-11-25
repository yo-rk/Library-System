import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Book implements Comparable<Book> {
    int bookId;
    String title;
    String author;
    String category;
    boolean isIssued;

    Book(int id, String t, String a, String c, boolean issued) {
        this.bookId = id;
        this.title = t == null ? "" : t.trim();
        this.author = a == null ? "" : a.trim();
        this.category = c == null ? "" : c.trim();
        this.isIssued = issued;
    }

    void markAsIssued() { this.isIssued = true; }
    void markAsReturned() { this.isIssued = false; }

    void display() {
        System.out.println("ID: " + bookId +
                " | Title: " + title +
                " | Author: " + author +
                " | Category: " + category +
                " | Issued: " + (isIssued ? "Yes" : "No"));
    }

    @Override
    public int compareTo(Book o) {
        return this.title.compareToIgnoreCase(o.title);
    }

    String toData() {
        // format: id|title|author|category|isIssued(1/0)
        return bookId + "|" + escape(title) + "|" + escape(author) + "|" + escape(category) + "|" + (isIssued ? "1" : "0");
    }

    static Book fromData(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 5) return null;
        try {
            int id = Integer.parseInt(p[0]);
            String t = unescape(p[1]);
            String a = unescape(p[2]);
            String c = unescape(p[3]);
            boolean issued = "1".equals(p[4]);
            return new Book(id, t, a, c, issued);
        } catch (Exception ex) {
            return null;
        }
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("|", " ");
    }

    private static String unescape(String s) {
        return s == null ? "" : s;
    }
}

class Member{
    int memberId;
    String name;
    String email;
    List<Integer> issuedBooks = new ArrayList<>();

    Member(int id, String n, String e) {
        this.memberId = id;
        this.name = n == null ? "" : n.trim();
        this.email = e == null ? "" : e.trim();
    }

    void display() {
        String books = issuedBooks.isEmpty() ? "None" : issuedBooks.stream().map(String::valueOf).collect(Collectors.joining(","));
        System.out.println("ID: " + memberId +
                " | Name: " + name +
                " | Email: " + email +
                " | Issued Books: " + books);
    }

    void addBook(int id) {
        if (!issuedBooks.contains(id)) issuedBooks.add(id);
    }

    boolean returnBook(int id) {
        return issuedBooks.remove(Integer.valueOf(id));
    }

    String toData() {
        String books = issuedBooks.isEmpty() ? "" : issuedBooks.stream().map(String::valueOf).collect(Collectors.joining(","));
        return memberId + "|" + escape(name) + "|" + escape(email) + "|" + books;
    }

    static Member fromData(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 3) return null;
        try {
            int id = Integer.parseInt(p[0]);
            String n = unescape(p[1]);
            String e = unescape(p[2]);
            Member m = new Member(id, n, e);
            if (p.length >= 4 && p[3] != null && !p[3].trim().isEmpty()) {
                String[] ids = p[3].split(",");
                for (String s : ids) {
                    try { m.issuedBooks.add(Integer.parseInt(s.trim())); } catch (Exception ignored) {}
                }
            }
            return m;
        } catch (Exception ex) {
            return null;
        }
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("|", " ");
    }

    private static String unescape(String s) {
        return s == null ? "" : s;
    }
}

public class LibrarySystem {
    private static final String BOOK_FILE = "books.txt";
    private static final String MEMBER_FILE = "members.txt";

    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Member> members = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");

    public static void main(String[] args) {
        LibrarySystem app = new LibrarySystem();
        app.run();
    }

    private void run() {
        loadFromFile();
        while (true) {
            printMenu();
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            int choice;
            try { choice = Integer.parseInt(line); } catch (NumberFormatException e) { System.out.println("Enter number 1-7"); continue; }

            switch (choice) {
                case 1 -> addBook();
                case 2 -> addMember();
                case 3 -> issueBook();
                case 4 -> returnBook();
                case 5 -> searchBooks();
                case 6 -> sortBooks();
                case 7 -> { saveToFile(); System.out.println("Saved. Exiting."); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n===== City Library Digital Management System =====");
        System.out.println("1. Add Book");
        System.out.println("2. Add Member");
        System.out.println("3. Issue Book");
        System.out.println("4. Return Book");
        System.out.println("5. Search Books");
        System.out.println("6. Sort Books");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    // ----- Operations -----
    private void addBook() {
        System.out.print("Enter Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Enter Category: ");
        String category = scanner.nextLine().trim();

        int id = generateBookId();
        Book b = new Book(id, title, author, category, false);
        books.put(id, b);
        saveToFile();
        System.out.println("Book added with ID: " + id);
    }

    private void addMember() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            System.out.println("Invalid email format.");
            return;
        }
        int id = generateMemberId();
        Member m = new Member(id, name, email);
        members.put(id, m);
        saveToFile();
        System.out.println("Member added with ID: " + id);
    }

    private void issueBook() {
        System.out.print("Enter Book ID: ");
        Integer bookId = readInt();
        if (bookId == null) return;
        System.out.print("Enter Member ID: ");
        Integer memberId = readInt();
        if (memberId == null) return;

        Book b = books.get(bookId);
        Member m = members.get(memberId);
        if (b == null) { System.out.println("Book ID not found."); return; }
        if (m == null) { System.out.println("Member ID not found."); return; }
        if (b.isIssued) { System.out.println("Book already issued."); return; }

        b.markAsIssued();
        m.addBook(bookId);
        saveToFile();
        System.out.println("Book issued successfully.");
    }

    private void returnBook() {
        System.out.print("Enter Book ID: ");
        Integer bookId = readInt();
        if (bookId == null) return;
        System.out.print("Enter Member ID: ");
        Integer memberId = readInt();
        if (memberId == null) return;

        Book b = books.get(bookId);
        Member m = members.get(memberId);
        if (b == null) { System.out.println("Book ID not found."); return; }
        if (m == null) { System.out.println("Member ID not found."); return; }

        b.markAsReturned();
        boolean removed = m.returnBook(bookId);
        saveToFile();
        System.out.println(removed ? "Book returned successfully." : "Book was not listed as issued to this member, but marked returned.");
    }

    private void searchBooks() {
        System.out.print("Enter search keyword (title/author/category): ");
        String key = scanner.nextLine().trim().toLowerCase();
        if (key.isEmpty()) { System.out.println("Empty search."); return; }

        List<Book> found = new ArrayList<>();
        for (Book b : books.values()) {
            if (b.title.toLowerCase().contains(key) || b.author.toLowerCase().contains(key) || b.category.toLowerCase().contains(key)) {
                found.add(b);
            }
        }
        if (found.isEmpty()) System.out.println("No books found.");
        else found.forEach(Book::display);
    }

    private void sortBooks() {
        System.out.println("Sort by: 1) Title  2) Author  3) Category");
        Integer c = readInt();
        if (c == null) return;
        List<Book> list = new ArrayList<>(books.values());
        switch (c) {
            case 1 -> Collections.sort(list);
            case 2 -> list.sort(Comparator.comparing(b -> b.author.toLowerCase()));
            case 3 -> list.sort(Comparator.comparing(b -> b.category.toLowerCase()));
            default -> { System.out.println("Invalid sort option."); return; }
        }
        list.forEach(Book::display);
    }

    // ----- Helpers -----
    private Integer readInt() {
        String s = scanner.nextLine().trim();
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { System.out.println("Invalid number."); return null; }
    }

    private int generateBookId() {
        // start IDs at 101 like sample interaction
        if (books.isEmpty()) return 101;
        return Collections.max(books.keySet()) + 1;
    }

    private int generateMemberId() {
        if (members.isEmpty()) return 1;
        return Collections.max(members.keySet()) + 1;
    }

    // ----- File I/O -----
    private void saveToFile() {
        saveBooks();
        saveMembers();
    }

    private void saveBooks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOK_FILE))) {
            for (Book b : books.values()) {
                bw.write(b.toData());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    private void saveMembers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEMBER_FILE))) {
            for (Member m : members.values()) {
                bw.write(m.toData());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        // load books
        File bf = new File(BOOK_FILE);
        if (bf.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(bf))) {
                String line;
                while ((line = br.readLine()) != null) {
                    Book b = Book.fromData(line);
                    if (b != null) books.put(b.bookId, b);
                }
            } catch (IOException e) {
                System.out.println("Error loading books: " + e.getMessage());
            }
        }

        // load members
        File mf = new File(MEMBER_FILE);
        if (mf.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(mf))) {
                String line;
                while ((line = br.readLine()) != null) {
                    Member m = Member.fromData(line);
                    if (m != null) members.put(m.memberId, m);
                }
            } catch (IOException e) {
                System.out.println("Error loading members: " + e.getMessage());
            }
        }
    }
}
