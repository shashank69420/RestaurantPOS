import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ManagerDashboard — Premium Swing UI for the restaurant manager.
 * 4-tab sidebar navigation + Dashboard Summary popup.
 * Custom modern scrollbars, paginated menu, payment flow.
 */
public class ManagerDashboard extends JFrame {

    private final Menu menu;
    private final Inventory inventory;
    private final Kitchen kitchen;
    private final Table[] tables;

    private CardLayout cardLayout;
    private JPanel mainContent;
    private JPanel sidebarPanel;
    private JButton activeNavButton = null;
    private int menuPage = 0;

    // Theme
    private static final Color BG      = RestaurantPOS.BG_DARK;
    private static final Color SFC     = RestaurantPOS.SURFACE;
    private static final Color SFC_L   = RestaurantPOS.SURFACE_LIGHT;
    private static final Color GOLD    = RestaurantPOS.ACCENT_GOLD;
    private static final Color GOLD_W  = RestaurantPOS.ACCENT_WARM;
    private static final Color TXT     = RestaurantPOS.TEXT_PRIMARY;
    private static final Color TXT2    = RestaurantPOS.TEXT_SECONDARY;
    private static final Color OK      = RestaurantPOS.SUCCESS_GREEN;
    private static final Color WARN    = RestaurantPOS.WARNING_AMBER;
    private static final Color ERR     = RestaurantPOS.DANGER_RED;
    private static final Color VEG     = RestaurantPOS.VEG_GREEN;
    private static final Color NVEG    = RestaurantPOS.NON_VEG_RED;

    private static final Color SCROLL_TRACK = new Color(30, 30, 34);
    private static final Color SCROLL_THUMB = new Color(70, 70, 78);
    private static final Color SCROLL_HOVER = new Color(100, 100, 110);

    private static final String C_MENU = "M", C_INV = "I", C_KIT = "K", C_TBL = "T";

    // ═══════════ CONSTRUCTOR ═══════════
    public ManagerDashboard(Menu menu, Inventory inv, Kitchen kitchen, Table[] tables) {
        this.menu = menu; this.inventory = inv; this.kitchen = kitchen; this.tables = tables;
        setTitle("L'Etoile  -  Manager Dashboard");
        setSize(1360, 900);
        setMinimumSize(new Dimension(1100, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(0, 0));
        buildSidebar(); buildContent();
        add(sidebarPanel, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
    }

    // ═══════════ MODERN SCROLLBAR ═══════════
    private void applyModernScroll(JScrollPane sp) {
        sp.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        sp.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        sp.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
    }

    /** Thin, rounded, dark scrollbar that blends with the dark theme. */
    private static class ModernScrollBarUI extends BasicScrollBarUI {
        @Override protected void configureScrollBarColors() {
            trackColor = SCROLL_TRACK;
        }
        @Override protected JButton createDecreaseButton(int orientation) { return zeroButton(); }
        @Override protected JButton createIncreaseButton(int orientation) { return zeroButton(); }
        private JButton zeroButton() {
            JButton b = new JButton(); b.setPreferredSize(new Dimension(0, 0)); return b;
        }
        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            g.setColor(SCROLL_TRACK); g.fillRect(r.x, r.y, r.width, r.height);
        }
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            if (r.isEmpty() || !scrollbar.isEnabled()) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isThumbRollover() ? SCROLL_HOVER : SCROLL_THUMB);
            int arc = Math.min(r.width, r.height);
            g2.fillRoundRect(r.x + 1, r.y + 1, r.width - 2, r.height - 2, arc, arc);
            g2.dispose();
        }
    }

    // ═══════════ SIDEBAR ═══════════
    private void buildSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(BG);
        sidebarPanel.setPreferredSize(new Dimension(220, 0));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(40, 40, 45)));

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 22)));
        sidebarPanel.add(cLabel("L'Etoile", RestaurantPOS.FONT_LOGO, GOLD));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        sidebarPanel.add(cLabel("FINE DINING", new Font("Segoe UI", Font.PLAIN, 10), TXT2));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(divider());
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        JButton b1 = nav("Menu",      C_MENU);
        JButton b2 = nav("Inventory", C_INV);
        JButton b3 = nav("Kitchen",   C_KIT);
        JButton b4 = nav("Tables",    C_TBL);
        sidebarPanel.add(b1); sidebarPanel.add(b2); sidebarPanel.add(b3); sidebarPanel.add(b4);
        setActiveNav(b1);

        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(divider());
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JButton bs = new JButton("Dashboard Summary");
        styleBtn(bs, GOLD, BG, 12);
        bs.setAlignmentX(Component.CENTER_ALIGNMENT);
        bs.setMaximumSize(new Dimension(190, 36));
        bs.addActionListener(e -> showDashboardSummary());
        bs.addMouseListener(hov(bs, GOLD_W, GOLD));
        sidebarPanel.add(bs);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private JButton nav(String text, String card) {
        JButton b = new JButton("   " + text);
        b.setFont(RestaurantPOS.FONT_BODY); b.setForeground(TXT2); b.setBackground(BG);
        b.setFocusPainted(false); b.setBorderPainted(false); b.setOpaque(true);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(220, 42));
        b.setPreferredSize(new Dimension(220, 42));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> { setActiveNav(b); refreshAll(card); });
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (b != activeNavButton) b.setBackground(SFC); }
            public void mouseExited(MouseEvent e)  { if (b != activeNavButton) b.setBackground(BG); }
        });
        return b;
    }

    private void setActiveNav(JButton b) {
        if (activeNavButton != null) { activeNavButton.setBackground(BG); activeNavButton.setForeground(TXT2); }
        activeNavButton = b; b.setBackground(SFC_L); b.setForeground(GOLD);
    }

    // ═══════════ CONTENT ═══════════
    private void buildContent() {
        cardLayout = new CardLayout(); mainContent = new JPanel(cardLayout);
        mainContent.setBackground(SFC);
        mainContent.add(buildMenuPanel(),  C_MENU);
        mainContent.add(buildInvPanel(),   C_INV);
        mainContent.add(buildKitPanel(),   C_KIT);
        mainContent.add(buildTblPanel(),   C_TBL);
    }
    private void refreshAll(String s) {
        mainContent.removeAll();
        mainContent.add(buildMenuPanel(),  C_MENU);
        mainContent.add(buildInvPanel(),   C_INV);
        mainContent.add(buildKitPanel(),   C_KIT);
        mainContent.add(buildTblPanel(),   C_TBL);
        cardLayout.show(mainContent, s);
        mainContent.revalidate(); mainContent.repaint();
    }

    // ═══════════════════════════════
    //  TAB 1 — MENU (paginated, 3-col)
    // ═══════════════════════════════
    private JPanel buildMenuPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(SFC); p.setBorder(new EmptyBorder(24, 28, 24, 28));

        List<String> cats = new ArrayList<>();
        for (MenuItem mi : menu.getItems()) if (!cats.contains(mi.getCategory())) cats.add(mi.getCategory());
        if (cats.isEmpty()) return p;
        if (menuPage >= cats.size()) menuPage = 0;
        if (menuPage < 0) menuPage = cats.size() - 1;
        String cat = cats.get(menuPage);

        // Header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false); hdr.setBorder(new EmptyBorder(0, 0, 14, 0));
        JPanel ttl = new JPanel(); ttl.setLayout(new BoxLayout(ttl, BoxLayout.Y_AXIS)); ttl.setOpaque(false);
        ttl.add(mkLabel("Menu", RestaurantPOS.FONT_HEADER, TXT));
        ttl.add(Box.createRigidArea(new Dimension(0, 3)));
        ttl.add(mkLabel(cat + "  (" + (menuPage+1) + "/" + cats.size() + ")", RestaurantPOS.FONT_SMALL, TXT2));
        hdr.add(ttl, BorderLayout.WEST);

        JPanel nv = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); nv.setOpaque(false);
        JButton prev = styledBtn("<  Prev", SFC_L, TXT); prev.setPreferredSize(new Dimension(85, 32));
        JButton next = styledBtn("Next  >", SFC_L, TXT); next.setPreferredSize(new Dimension(85, 32));
        prev.addActionListener(e -> { menuPage--; refreshAll(C_MENU); });
        next.addActionListener(e -> { menuPage++; refreshAll(C_MENU); });
        prev.addMouseListener(hov(prev, SFC, SFC_L));
        next.addMouseListener(hov(next, SFC, SFC_L));
        nv.add(prev); nv.add(next);
        hdr.add(nv, BorderLayout.EAST);
        p.add(hdr, BorderLayout.NORTH);

        // Cards grid — 3 columns
        List<MenuItem> items = new ArrayList<>();
        for (MenuItem mi : menu.getItems()) if (mi.getCategory().equals(cat)) items.add(mi);

        JPanel grid = new JPanel(new GridLayout(0, 3, 14, 14));
        grid.setOpaque(false); grid.setBorder(new EmptyBorder(6, 4, 6, 4));
        for (MenuItem mi : items) grid.add(menuCard(mi));
        // fill remaining cells
        int rem = items.size() % 3;
        if (rem != 0) for (int i = 0; i < 3 - rem; i++) { JPanel f = new JPanel(); f.setOpaque(false); grid.add(f); }

        JScrollPane sc = new JScrollPane(grid);
        sc.setBorder(null); sc.getViewport().setBackground(SFC); applyModernScroll(sc);
        p.add(sc, BorderLayout.CENTER);
        return p;
    }

    private JPanel menuCard(MenuItem mi) {
        boolean avail = inventory.canPrepare(mi.getName().toLowerCase(), 1);
        JPanel c = new JPanel(new BorderLayout(8, 0));
        c.setBackground(SFC_L);
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, mi.isVegetarian() ? VEG : NVEG),
            new EmptyBorder(12, 14, 12, 14)));

        JPanel left = new JPanel(); left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS)); left.setOpaque(false);
        left.add(mkLabel(mi.getName(), new Font("Segoe UI", Font.BOLD, 14), TXT));
        left.add(Box.createRigidArea(new Dimension(0, 2)));
        left.add(mkLabel(mi.isVegetarian() ? "Vegetarian" : "Non-Veg", new Font("Segoe UI", Font.ITALIC, 10), mi.isVegetarian() ? VEG : NVEG));
        left.add(Box.createRigidArea(new Dimension(0, 6)));
        left.add(mkLabel("$" + mi.getPrice().toPlainString(), new Font("Segoe UI", Font.BOLD, 16), GOLD));

        JPanel right = new JPanel(); right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS)); right.setOpaque(false);
        JLabel av = new JLabel(avail ? "Available" : "Sold Out");
        av.setFont(new Font("Segoe UI", Font.BOLD, 10)); av.setForeground(avail ? OK : ERR);
        av.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(av); right.add(Box.createRigidArea(new Dimension(0, 6)));
        JButton rb = styledBtn("Recipe", new Color(55, 55, 62), TXT);
        rb.setFont(new Font("Segoe UI", Font.BOLD, 11));
        rb.setAlignmentX(Component.CENTER_ALIGNMENT);
        rb.addActionListener(e -> showRecipeDialog(mi.getName()));
        rb.addMouseListener(hov(rb, GOLD, new Color(55, 55, 62)));
        right.add(rb);

        c.add(left, BorderLayout.CENTER); c.add(right, BorderLayout.EAST);
        return c;
    }

    private void showRecipeDialog(String name) {
        Map<String, Integer> recipe = inventory.getRecipe(name.toLowerCase());
        MenuItem mi = menu.findItem(name);
        JDialog d = new JDialog(this, "Recipe: " + name, true);
        d.setSize(460, 360); d.setLocationRelativeTo(this);
        JPanel c = new JPanel(new BorderLayout(0, 10));
        c.setBackground(BG);
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(GOLD, 2), new EmptyBorder(20, 24, 20, 24)));
        JPanel h = new JPanel(new BorderLayout()); h.setOpaque(false);
        h.add(mkLabel(name, RestaurantPOS.FONT_HEADER, GOLD), BorderLayout.WEST);
        if (mi != null) h.add(mkLabel(mi.isVegetarian() ? "  VEG" : "  NON-VEG", new Font("Segoe UI", Font.BOLD, 12), mi.isVegetarian() ? VEG : NVEG), BorderLayout.EAST);
        c.add(h, BorderLayout.NORTH);
        JPanel body = new JPanel(); body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS)); body.setBackground(BG);
        if (recipe == null || recipe.isEmpty()) { body.add(mkLabel("No recipe defined.", RestaurantPOS.FONT_BODY, TXT2)); }
        else {
            body.add(mkLabel("Ingredients per serving:", RestaurantPOS.FONT_SMALL, TXT2));
            body.add(Box.createRigidArea(new Dimension(0, 8)));
            for (Map.Entry<String, Integer> e : recipe.entrySet()) {
                int s = inventory.getStockSnapshot().getOrDefault(e.getKey(), 0);
                JPanel r = new JPanel(new BorderLayout()); r.setOpaque(false); r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
                r.add(mkLabel("  " + e.getValue() + "x  " + e.getKey(), RestaurantPOS.FONT_BODY, TXT), BorderLayout.WEST);
                r.add(mkLabel(s + " in stock  ", RestaurantPOS.FONT_SMALL, s > 5 ? OK : (s > 0 ? WARN : ERR)), BorderLayout.EAST);
                body.add(r); body.add(Box.createRigidArea(new Dimension(0, 2)));
            }
        }
        JScrollPane sc = new JScrollPane(body); sc.setBorder(null); sc.getViewport().setBackground(BG); applyModernScroll(sc);
        c.add(sc, BorderLayout.CENTER);
        JPanel bt = new JPanel(new FlowLayout(FlowLayout.CENTER)); bt.setOpaque(false);
        JButton cl = styledBtn("Close", SFC_L, TXT); cl.addActionListener(e -> d.dispose()); bt.add(cl);
        c.add(bt, BorderLayout.SOUTH);
        d.add(c); d.setVisible(true);
    }

    // ═══════════════════════════════
    //  TAB 2 — INVENTORY (2-col grid)
    // ═══════════════════════════════
    private JPanel buildInvPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(SFC); p.setBorder(new EmptyBorder(24, 28, 24, 28));
        JPanel hdr = new JPanel(); hdr.setLayout(new BoxLayout(hdr, BoxLayout.Y_AXIS)); hdr.setOpaque(false); hdr.setBorder(new EmptyBorder(0, 0, 14, 0));
        hdr.add(mkLabel("Inventory", RestaurantPOS.FONT_HEADER, TXT));
        hdr.add(Box.createRigidArea(new Dimension(0, 3)));
        hdr.add(mkLabel("Ingredients grouped by category", RestaurantPOS.FONT_SMALL, TXT2));
        p.add(hdr, BorderLayout.NORTH);

        Map<String, Integer> stock = inventory.getStockSnapshot();
        Map<String, String> catMap = inventory.getCategorySnapshot();
        Map<String, List<String>> groups = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> e : stock.entrySet()) {
            String cat = catMap.getOrDefault(e.getKey(), "Other");
            groups.computeIfAbsent(cat, k -> new ArrayList<>()).add(e.getKey());
        }

        JPanel grid = new JPanel(new GridLayout(0, 2, 16, 12));
        grid.setOpaque(false); grid.setBorder(new EmptyBorder(4, 2, 4, 2));
        for (Map.Entry<String, List<String>> g : groups.entrySet()) {
            JPanel cp = new JPanel(); cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS)); cp.setBackground(SFC_L);
            cp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, GOLD), new EmptyBorder(10, 12, 10, 12)));
            cp.add(mkLabel(g.getKey(), new Font("Segoe UI", Font.BOLD, 13), GOLD));
            cp.add(Box.createRigidArea(new Dimension(0, 6)));
            for (String ing : g.getValue()) {
                int q = stock.get(ing);
                JPanel row = new JPanel(new BorderLayout()); row.setOpaque(false); row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
                row.add(mkLabel(ing, RestaurantPOS.FONT_SMALL, TXT), BorderLayout.WEST);
                String st; Color sc;
                if (q == 0) { st = "OUT"; sc = ERR; } else if (q <= 5) { st = q+" LOW"; sc = ERR; }
                else if (q <= 15) { st = String.valueOf(q); sc = WARN; } else { st = String.valueOf(q); sc = OK; }
                row.add(mkLabel(st+"  ", new Font("Segoe UI", Font.BOLD, 11), sc), BorderLayout.EAST);
                cp.add(row); cp.add(Box.createRigidArea(new Dimension(0, 2)));
            }
            grid.add(cp);
        }
        if (groups.size() % 2 != 0) { JPanel f = new JPanel(); f.setOpaque(false); grid.add(f); }
        JScrollPane sc = new JScrollPane(grid); sc.setBorder(null); sc.getViewport().setBackground(SFC); applyModernScroll(sc);
        p.add(sc, BorderLayout.CENTER);
        return p;
    }

    // ═══════════════════════════════
    //  TAB 3 — KITCHEN
    // ═══════════════════════════════
    private JPanel buildKitPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(SFC); p.setBorder(new EmptyBorder(24, 28, 24, 28));
        JPanel hdr = new JPanel(new BorderLayout()); hdr.setOpaque(false); hdr.setBorder(new EmptyBorder(0, 0, 14, 0));
        JPanel ttl = new JPanel(); ttl.setLayout(new BoxLayout(ttl, BoxLayout.Y_AXIS)); ttl.setOpaque(false);
        ttl.add(mkLabel("Kitchen Orders", RestaurantPOS.FONT_HEADER, TXT));
        ttl.add(Box.createRigidArea(new Dimension(0, 3)));
        ttl.add(mkLabel("Manage preparation, completion, and payment", RestaurantPOS.FONT_SMALL, TXT2));
        hdr.add(ttl, BorderLayout.WEST);
        JButton bn = styledBtn("+ New Order", GOLD, BG); bn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bn.setPreferredSize(new Dimension(140, 34));
        bn.addActionListener(e -> showNewOrderDialog()); bn.addMouseListener(hov(bn, GOLD_W, GOLD));
        JPanel tbr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4)); tbr.setOpaque(false); tbr.add(bn);
        hdr.add(tbr, BorderLayout.EAST);
        p.add(hdr, BorderLayout.NORTH);

        JPanel cards = new JPanel(); cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS)); cards.setBackground(SFC);
        List<KitchenOrder> orders = kitchen.getActiveOrders();
        if (orders.isEmpty()) {
            JLabel lb = mkLabel("No active orders. Click '+ New Order' to create one.", RestaurantPOS.FONT_BODY, TXT2);
            lb.setAlignmentX(Component.CENTER_ALIGNMENT); lb.setBorder(new EmptyBorder(60, 0, 0, 0)); cards.add(lb);
        } else {
            for (KitchenOrder ko : orders) { cards.add(orderCard(ko)); cards.add(Box.createRigidArea(new Dimension(0, 10))); }
        }
        JScrollPane sc = new JScrollPane(cards); sc.setBorder(null); sc.getViewport().setBackground(SFC); applyModernScroll(sc);
        p.add(sc, BorderLayout.CENTER);
        return p;
    }

    private JPanel orderCard(KitchenOrder ko) {
        OrderState st = ko.getStatus(); Order ord = ko.getOrder();
        JPanel c = new JPanel(new BorderLayout(12, 0)); c.setBackground(SFC_L);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, statusColor(st)), new EmptyBorder(12, 16, 12, 16)));
        JPanel info = new JPanel(); info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS)); info.setOpaque(false);
        info.add(mkLabel("Order #" + ord.getOrderId() + "  |  Table " + ord.getTableNumber(), RestaurantPOS.FONT_SUBHEAD, TXT));
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        StringBuilder sb = new StringBuilder();
        for (OrderItem oi : ord.getItems()) { if (sb.length() > 0) sb.append("  |  "); sb.append(oi.getQuantity()).append("x ").append(oi.getMenuItem().getName()); }
        info.add(mkLabel(sb.toString(), RestaurantPOS.FONT_SMALL, TXT2));
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        info.add(mkLabel("Total: $" + ord.getTotal().toPlainString(), RestaurantPOS.FONT_BODY, GOLD));

        JPanel right = new JPanel(); right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS)); right.setOpaque(false); right.setBorder(new EmptyBorder(4, 6, 4, 0));
        JLabel badge = new JLabel("  " + statusText(st) + "  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11)); badge.setForeground(statusColor(st)); badge.setOpaque(true);
        badge.setBackground(blend(statusColor(st), 35));
        badge.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(statusColor(st), 1, true), new EmptyBorder(3, 10, 3, 10)));
        badge.setHorizontalAlignment(SwingConstants.CENTER); badge.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(badge); right.add(Box.createRigidArea(new Dimension(0, 8)));
        JButton act = actionBtn(ko, st);
        if (act != null) { act.setAlignmentX(Component.CENTER_ALIGNMENT); right.add(act); }
        c.add(info, BorderLayout.CENTER); c.add(right, BorderLayout.EAST);
        return c;
    }

    private JButton actionBtn(KitchenOrder ko, OrderState st) {
        switch (st) {
            case SENT_TO_KITCHEN -> { JButton b = styledBtn("Start Preparing", GOLD, BG); b.addActionListener(e -> { ko.startPreparing(); refreshAll(C_KIT); }); return b; }
            case PREPARING -> { JButton b = styledBtn("Mark as Ready", OK, Color.WHITE); b.addActionListener(e -> {
                ko.markReady();
                for (Table t : tables) if (t.getCurrentOrder() != null && t.getCurrentOrder().getOrderId() == ko.getOrder().getOrderId()) { t.markReadyForBill(); break; }
                refreshAll(C_KIT);
            }); return b; }
            case READY -> { JButton b = styledBtn("Process Payment", WARN, BG); b.addActionListener(e -> showPaymentDialog(ko)); return b; }
            default -> { return null; }
        }
    }

    // ═══════════ PAYMENT DIALOG ═══════════
    private void showPaymentDialog(KitchenOrder ko) {
        JDialog d = new JDialog(this, "Process Payment", true);
        d.setSize(440, 340); d.setLocationRelativeTo(this);
        JPanel c = new JPanel(new BorderLayout(0, 14));
        c.setBackground(BG);
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(GOLD, 2), new EmptyBorder(22, 26, 22, 26)));
        JPanel hdr = new JPanel(); hdr.setLayout(new BoxLayout(hdr, BoxLayout.Y_AXIS)); hdr.setOpaque(false);
        hdr.add(mkLabel("Payment for Order #" + ko.getOrder().getOrderId(), RestaurantPOS.FONT_SUBHEAD, TXT));
        hdr.add(Box.createRigidArea(new Dimension(0, 4)));
        hdr.add(mkLabel("Table " + ko.getOrder().getTableNumber() + "  |  Total: $" + ko.getOrder().getTotal().toPlainString(), RestaurantPOS.FONT_BODY, GOLD));
        hdr.add(Box.createRigidArea(new Dimension(0, 4)));
        hdr.add(mkLabel("Select payment method:", RestaurantPOS.FONT_SMALL, TXT2));
        c.add(hdr, BorderLayout.NORTH);
        JPanel m = new JPanel(new GridLayout(1, 3, 14, 0)); m.setOpaque(false);
        m.add(payOpt("Cash", new Color(76, 175, 80), "$", ko, d));
        m.add(payOpt("Card", new Color(66, 133, 244), "C", ko, d));
        m.add(payOpt("UPI",  new Color(156, 39, 176), "U", ko, d));
        c.add(m, BorderLayout.CENTER);
        JPanel bt = new JPanel(new FlowLayout(FlowLayout.CENTER)); bt.setOpaque(false);
        JButton cl = styledBtn("Cancel", SFC_L, TXT); cl.addActionListener(e -> d.dispose()); bt.add(cl);
        c.add(bt, BorderLayout.SOUTH);
        d.add(c); d.setVisible(true);
    }

    private JPanel payOpt(String method, Color color, String icon, KitchenOrder ko, JDialog parent) {
        JPanel c = new JPanel(); c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS)); c.setBackground(SFC);
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1, true), new EmptyBorder(18, 8, 18, 8)));
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel ic = cLabel(icon, new Font("Segoe UI", Font.BOLD, 26), color);
        JLabel lb = cLabel(method, new Font("Segoe UI", Font.BOLD, 13), TXT);
        c.add(Box.createVerticalGlue()); c.add(ic); c.add(Box.createRigidArea(new Dimension(0, 6))); c.add(lb); c.add(Box.createVerticalGlue());
        c.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { c.setBackground(SFC_L); c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color, 2, true), new EmptyBorder(17, 7, 17, 7))); }
            public void mouseExited(MouseEvent e)  { c.setBackground(SFC);   c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1, true), new EmptyBorder(18, 8, 18, 8))); }
            public void mouseClicked(MouseEvent e) {
                ko.markPaid();
                for (Table t : tables) if (t.getCurrentOrder() != null && t.getCurrentOrder().getOrderId() == ko.getOrder().getOrderId()) { t.clearTable(); break; }
                kitchen.archiveOrder(ko); parent.dispose();
                JOptionPane.showMessageDialog(ManagerDashboard.this, "Payment of $" + ko.getOrder().getTotal().toPlainString() + " received via " + method + ".\nOrder #" + ko.getOrder().getOrderId() + " completed.", "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
                refreshAll(C_KIT);
            }
        });
        return c;
    }

    // ═══════════ NEW ORDER DIALOG ═══════════
    private void showNewOrderDialog() {
        List<Table> avail = new ArrayList<>();
        for (Table t : tables) if (t.getState() == TableState.AVAILABLE) avail.add(t);
        if (avail.isEmpty()) { JOptionPane.showMessageDialog(this, "No available tables."); return; }
        JDialog d = new JDialog(this, "Create New Order", true);
        d.setSize(620, 580); d.setLocationRelativeTo(this);
        JPanel c = new JPanel(new BorderLayout(0, 10)); c.setBackground(SFC); c.setBorder(new EmptyBorder(20, 24, 20, 24));
        JPanel top = new JPanel(); top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS)); top.setOpaque(false);
        top.add(mkLabel("New Order", RestaurantPOS.FONT_HEADER, TXT));
        top.add(Box.createRigidArea(new Dimension(0, 8)));
        JPanel tr = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); tr.setOpaque(false);
        tr.add(mkLabel("Table:", RestaurantPOS.FONT_BODY, TXT));
        JComboBox<String> combo = new JComboBox<>(); for (Table t : avail) combo.addItem("Table " + t.getTableNumber());
        combo.setFont(RestaurantPOS.FONT_BODY); tr.add(combo); tr.setAlignmentX(Component.LEFT_ALIGNMENT); top.add(tr);
        c.add(top, BorderLayout.NORTH);

        JPanel items = new JPanel(); items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS)); items.setBackground(SFC);
        List<JSpinner> spins = new ArrayList<>(); List<MenuItem> mItems = new ArrayList<>();
        for (MenuItem mi : menu.getItems()) {
            if (!inventory.canPrepare(mi.getName().toLowerCase(), 1)) continue;
            mItems.add(mi);
            JPanel row = new JPanel(new BorderLayout(6, 0)); row.setBackground(SFC_L);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
            row.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(48, 48, 52)), new EmptyBorder(3, 10, 3, 10)));
            JPanel lf = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0)); lf.setOpaque(false);
            lf.add(mkLabel(mi.isVegetarian() ? "V" : "N", new Font("Segoe UI", Font.BOLD, 10), mi.isVegetarian() ? VEG : NVEG));
            lf.add(mkLabel(mi.getName() + "  $" + mi.getPrice().toPlainString(), RestaurantPOS.FONT_SMALL, TXT));
            JSpinner sp = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1)); sp.setFont(RestaurantPOS.FONT_SMALL); sp.setPreferredSize(new Dimension(55, 24));
            spins.add(sp); row.add(lf, BorderLayout.CENTER); row.add(sp, BorderLayout.EAST); items.add(row);
        }
        JScrollPane sc = new JScrollPane(items); sc.setBorder(BorderFactory.createLineBorder(new Color(48, 48, 52)));
        sc.getViewport().setBackground(SFC); applyModernScroll(sc);
        c.add(sc, BorderLayout.CENTER);
        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); btm.setOpaque(false); btm.setBorder(new EmptyBorder(8, 0, 0, 0));
        JButton cancel = styledBtn("Cancel", SFC_L, TXT); cancel.addActionListener(e -> d.dispose());
        JButton submit = styledBtn("Submit Order", GOLD, BG); submit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submit.addActionListener(e -> {
            List<OrderItem> sel = new ArrayList<>();
            for (int i = 0; i < mItems.size(); i++) { int q = (int) spins.get(i).getValue(); if (q > 0) sel.add(new OrderItem(mItems.get(i), q)); }
            if (sel.isEmpty()) { JOptionPane.showMessageDialog(d, "Select at least one item."); return; }
            Table tbl = avail.get(combo.getSelectedIndex());
            tbl.seatCustomers(); Order order = tbl.getCurrentOrder();
            for (OrderItem oi : sel) order.addItem(oi);
            order.submit(); tbl.markWaitingForFood();
            for (OrderItem oi : sel) { try { inventory.deduceStockForOrder(oi.getMenuItem().getName().toLowerCase(), oi.getQuantity()); } catch (IllegalStateException ex) { JOptionPane.showMessageDialog(d, "Stock: " + ex.getMessage()); } }
            kitchen.receiveOrder(new KitchenOrder(order)); d.dispose(); refreshAll(C_KIT);
        });
        btm.add(cancel); btm.add(submit); c.add(btm, BorderLayout.SOUTH);
        d.add(c); d.setVisible(true);
    }

    // ═══════════════════════════════
    //  TAB 4 — TABLES (5x3 grid)
    // ═══════════════════════════════
    private JPanel buildTblPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(SFC); p.setBorder(new EmptyBorder(24, 28, 24, 28));
        int occ = 0; for (Table t : tables) if (t.getState() != TableState.AVAILABLE) occ++;
        JPanel hdr = new JPanel(); hdr.setLayout(new BoxLayout(hdr, BoxLayout.Y_AXIS)); hdr.setOpaque(false); hdr.setBorder(new EmptyBorder(0, 0, 18, 0));
        hdr.add(mkLabel("Tables", RestaurantPOS.FONT_HEADER, TXT));
        hdr.add(Box.createRigidArea(new Dimension(0, 3)));
        hdr.add(mkLabel(occ + " of " + tables.length + " tables occupied", RestaurantPOS.FONT_SMALL, TXT2));
        p.add(hdr, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 5, 20, 20));
        grid.setOpaque(false); grid.setBorder(new EmptyBorder(6, 12, 6, 12));
        for (Table t : tables) grid.add(tblCard(t));
        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    private JPanel tblCard(Table t) {
        TableState st = t.getState(); Color ac = tblColor(st);
        JPanel c = new JPanel(); c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS)); c.setBackground(SFC_L);
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ac, 2, true), new EmptyBorder(10, 10, 10, 10)));
        c.add(Box.createVerticalGlue());
        c.add(cLabel("T" + t.getTableNumber(), new Font("Segoe UI", Font.BOLD, 16), TXT));
        c.add(Box.createRigidArea(new Dimension(0, 4)));
        c.add(cLabel(tblText(st), new Font("Segoe UI", Font.BOLD, 9), ac));

        if (t.getCurrentOrder() != null) {
            Order ord = t.getCurrentOrder();
            c.add(Box.createRigidArea(new Dimension(0, 5)));
            int cnt = 0; for (OrderItem oi : ord.getItems()) cnt += oi.getQuantity();
            c.add(cLabel(cnt + " items | $" + ord.getTotal().toPlainString(), new Font("Segoe UI", Font.PLAIN, 9), GOLD));
            for (KitchenOrder ko : kitchen.getActiveOrders()) {
                if (ko.getOrder().getOrderId() == ord.getOrderId()) {
                    c.add(Box.createRigidArea(new Dimension(0, 2)));
                    c.add(cLabel(statusText(ko.getStatus()), new Font("Segoe UI", Font.BOLD, 9), statusColor(ko.getStatus())));
                    break;
                }
            }
        } else {
            c.add(Box.createRigidArea(new Dimension(0, 5)));
            c.add(cLabel("Empty", new Font("Segoe UI", Font.PLAIN, 9), new Color(70, 70, 80)));
        }
        c.add(Box.createVerticalGlue());
        return c;
    }

    // ═══════════ DASHBOARD SUMMARY ═══════════
    private void showDashboardSummary() {
        JDialog d = new JDialog(this, "Dashboard Summary", true);
        d.setSize(560, 500); d.setLocationRelativeTo(this);
        JPanel c = new JPanel(new BorderLayout(0, 10));
        c.setBackground(BG);
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(GOLD, 2), new EmptyBorder(22, 26, 22, 26)));
        c.add(mkLabel("Dashboard Summary", RestaurantPOS.FONT_HEADER, GOLD), BorderLayout.NORTH);

        JPanel body = new JPanel(); body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS)); body.setBackground(BG); body.setBorder(new EmptyBorder(8, 0, 0, 0));

        int occ = 0; for (Table t : tables) if (t.getState() != TableState.AVAILABLE) occ++;
        sumRow(body, "Table Occupancy", occ + " / " + tables.length, occ > tables.length / 2 ? WARN : OK);
        int act = kitchen.getActiveOrders().size();
        sumRow(body, "Active Kitchen Orders", act + " in progress", act > 4 ? ERR : (act > 0 ? GOLD : OK));
        BigDecimal rev = BigDecimal.ZERO;
        for (KitchenOrder ko : kitchen.getActiveOrders()) rev = rev.add(ko.getOrder().getTotal());
        sumRow(body, "Pending Revenue", "$" + rev.toPlainString(), GOLD);

        List<String> warns = inventory.getLowStockWarnings();
        body.add(Box.createRigidArea(new Dimension(0, 14)));

        JPanel wh = new JPanel(new BorderLayout()); wh.setBackground(blend(warns.isEmpty() ? OK : ERR, 25));
        wh.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34)); wh.setBorder(new EmptyBorder(7, 14, 7, 14));
        Color wc = warns.isEmpty() ? OK : ERR;
        wh.add(mkLabel("Low Stock Warnings", new Font("Segoe UI", Font.BOLD, 13), wc), BorderLayout.WEST);
        wh.add(mkLabel(String.valueOf(warns.size()), new Font("Segoe UI", Font.BOLD, 13), wc), BorderLayout.EAST);
        body.add(wh); body.add(Box.createRigidArea(new Dimension(0, 4)));

        if (warns.isEmpty()) {
            JPanel ok = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 5)); ok.setBackground(SFC); ok.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            ok.add(mkLabel("All ingredients sufficiently stocked.", RestaurantPOS.FONT_BODY, OK));
            body.add(ok);
        } else {
            for (String w : warns) {
                JPanel r = new JPanel(new BorderLayout()); r.setBackground(SFC); r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
                r.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, ERR), new EmptyBorder(4, 12, 4, 12)));
                r.add(mkLabel(w, RestaurantPOS.FONT_SMALL, ERR), BorderLayout.WEST);
                body.add(r); body.add(Box.createRigidArea(new Dimension(0, 2)));
            }
        }

        JScrollPane sc = new JScrollPane(body); sc.setBorder(null); sc.getViewport().setBackground(BG); applyModernScroll(sc);
        c.add(sc, BorderLayout.CENTER);
        JPanel bt = new JPanel(new FlowLayout(FlowLayout.CENTER)); bt.setOpaque(false);
        JButton cl = styledBtn("Close", SFC_L, TXT); cl.addActionListener(e -> d.dispose()); bt.add(cl);
        c.add(bt, BorderLayout.SOUTH);
        d.add(c); d.setVisible(true);
    }

    private void sumRow(JPanel p, String l, String v, Color vc) {
        JPanel r = new JPanel(new BorderLayout()); r.setBackground(SFC); r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44)); r.setBorder(new EmptyBorder(10, 14, 10, 14));
        r.add(mkLabel(l, RestaurantPOS.FONT_BODY, TXT2), BorderLayout.WEST);
        r.add(mkLabel(v, new Font("Segoe UI", Font.BOLD, 14), vc), BorderLayout.EAST);
        p.add(r); p.add(Box.createRigidArea(new Dimension(0, 4)));
    }

    // ═══════════ UTILITIES ═══════════
    private JButton styledBtn(String t, Color bg, Color fg) { JButton b = new JButton(t); styleBtn(b, bg, fg, 13); return b; }
    private void styleBtn(JButton b, Color bg, Color fg, int sz) { b.setFont(new Font("Segoe UI", Font.BOLD, sz)); b.setBackground(bg); b.setForeground(fg); b.setFocusPainted(false); b.setBorderPainted(false); b.setOpaque(true); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); b.setBorder(new EmptyBorder(7, 14, 7, 14)); }
    private JLabel mkLabel(String t, Font f, Color c) { JLabel l = new JLabel(t); l.setFont(f); l.setForeground(c); return l; }
    private JLabel cLabel(String t, Font f, Color c) { JLabel l = mkLabel(t, f, c); l.setAlignmentX(Component.CENTER_ALIGNMENT); return l; }
    private JSeparator divider() { JSeparator s = new JSeparator(); s.setMaximumSize(new Dimension(220, 1)); s.setForeground(new Color(45, 45, 50)); return s; }
    private MouseAdapter hov(JButton b, Color h, Color n) { return new MouseAdapter() { public void mouseEntered(MouseEvent e) { b.setBackground(h); } public void mouseExited(MouseEvent e) { b.setBackground(n); } }; }

    private String statusText(OrderState s) { return switch (s) { case SENT_TO_KITCHEN -> "Queued"; case PREPARING -> "Preparing"; case READY -> "Ready"; case PAID -> "Paid"; }; }
    private Color statusColor(OrderState s) { return switch (s) { case SENT_TO_KITCHEN -> WARN; case PREPARING -> GOLD; case READY -> OK; case PAID -> TXT2; }; }
    private String tblText(TableState s) { return switch (s) { case AVAILABLE -> "Available"; case OCCUPIED -> "Occupied"; case WAITING_FOR_FOOD -> "Waiting"; case READY_FOR_BILL -> "Ready for Bill"; }; }
    private Color tblColor(TableState s) { return switch (s) { case AVAILABLE -> OK; case OCCUPIED -> GOLD; case WAITING_FOR_FOOD -> WARN; case READY_FOR_BILL -> ERR; }; }
    private Color blend(Color c, int a) { return new Color((c.getRed()*a+SFC_L.getRed()*(255-a))/255, (c.getGreen()*a+SFC_L.getGreen()*(255-a))/255, (c.getBlue()*a+SFC_L.getBlue()*(255-a))/255); }
}
