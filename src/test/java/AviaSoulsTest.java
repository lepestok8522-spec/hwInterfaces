import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AviaSoulsTest {

    @Test
    public void testCompareToByPrice() {
        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);
        Ticket ticket2 = new Ticket("DME", "LED", 4500, 1400, 1530);
        Ticket ticket3 = new Ticket("DME", "LED", 6000, 900, 1130);


        assertTrue(ticket2.compareTo(ticket1) < 0);  // ticket2 дешевле
        assertTrue(ticket1.compareTo(ticket3) < 0);  // ticket1 дешевле
        assertTrue(ticket3.compareTo(ticket1) > 0);  // ticket3 дороже


        Ticket ticket6 = new Ticket("DME", "LED", 5000, 1000, 1200);
        assertEquals(0, ticket1.compareTo(ticket6));
    }

    @Test
    public void testSearchSortByPrice() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);
        Ticket ticket2 = new Ticket("DME", "LED", 4500, 1400, 1530);
        Ticket ticket3 = new Ticket("DME", "LED", 6000, 900, 1130);
        Ticket ticket4 = new Ticket("SVO", "KZN", 3000, 800, 1000);
        Ticket ticket5 = new Ticket("DME", "LED", 5500, 1600, 1800);

        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);
        manager.add(ticket4);
        manager.add(ticket5);

        Ticket[] result = manager.search("DME", "LED");

        assertEquals(4, result.length);


        assertEquals(4500, result[0].getPrice());
        assertEquals(5000, result[1].getPrice());
        assertEquals(5500, result[2].getPrice());
        assertEquals(6000, result[3].getPrice());
    }

    @Test
    public void testSearchNoResults() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);
        Ticket ticket2 = new Ticket("SVO", "KZN", 3000, 800, 1000);

        manager.add(ticket1);
        manager.add(ticket2);

        Ticket[] result = manager.search("DME", "GOJ");
        assertEquals(0, result.length);

        Ticket[] result2 = manager.search("SVO", "KZN");
        assertEquals(1, result2.length);
        assertEquals(3000, result2[0].getPrice());
    }

    @Test
    public void testTicketTimeComparator() {
        TicketTimeComparator comparator = new TicketTimeComparator();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);  // полет 2 часа
        Ticket ticket2 = new Ticket("DME", "LED", 4500, 1400, 1530);  // полет 1.5 часа
        Ticket ticket3 = new Ticket("DME", "LED", 6000, 900, 1130);    // полет 2.5 часа
        Ticket ticket5 = new Ticket("DME", "LED", 5500, 1600, 1800);   // полет 2 часа


        assertTrue(comparator.compare(ticket2, ticket1) < 0);
        assertTrue(comparator.compare(ticket1, ticket3) < 0);
        assertEquals(0, comparator.compare(ticket1, ticket5));
    }

    @Test
    public void testSearchAndSortByTime() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);  // 2 часа
        Ticket ticket2 = new Ticket("DME", "LED", 4500, 1400, 1530);  // 1.5 часа
        Ticket ticket3 = new Ticket("DME", "LED", 6000, 900, 1130);    // 2.5 часа
        Ticket ticket4 = new Ticket("SVO", "KZN", 3000, 800, 1000);
        Ticket ticket5 = new Ticket("DME", "LED", 5500, 1600, 1800);   // 2 часа

        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);
        manager.add(ticket4);
        manager.add(ticket5);

        TicketTimeComparator comparator = new TicketTimeComparator();
        Ticket[] result = manager.searchAndSortBy("DME", "LED", comparator);

        assertEquals(4, result.length);


        assertEquals(ticket2, result[0]);


        boolean hasTicket1 = false;
        boolean hasTicket5 = false;


        if (result[1].getPrice() == 5000 && result[2].getPrice() == 5500) {
            hasTicket1 = true;
            hasTicket5 = true;
        } else if (result[1].getPrice() == 5500 && result[2].getPrice() == 5000) {
            hasTicket1 = true;
            hasTicket5 = true;
        }

        assertTrue(hasTicket1 && hasTicket5);
        assertEquals(ticket3, result[3]);
    }

    @Test
    public void testSearchAndSortByTimeNoResults() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);
        manager.add(ticket1);

        TicketTimeComparator comparator = new TicketTimeComparator();
        Ticket[] result = manager.searchAndSortBy("DME", "GOJ", comparator);
        assertEquals(0, result.length);
    }

    @Test
    public void testFindAll() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);
        Ticket ticket2 = new Ticket("SVO", "KZN", 3000, 800, 1000);

        manager.add(ticket1);
        manager.add(ticket2);

        Ticket[] allTickets = manager.findAll();
        assertEquals(2, allTickets.length);
    }

    @Test
    public void testAddMultipleTickets() {
        AviaSouls newManager = new AviaSouls();
        assertEquals(0, newManager.findAll().length);

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);
        Ticket ticket2 = new Ticket("SVO", "KZN", 3000, 800, 1000);

        newManager.add(ticket1);
        assertEquals(1, newManager.findAll().length);

        newManager.add(ticket2);
        assertEquals(2, newManager.findAll().length);
    }

    @Test
    public void testSortingWithEqualPrices() {
        AviaSouls manager = new AviaSouls();


        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);
        Ticket ticket2 = new Ticket("DME", "LED", 5000, 1400, 1530);
        Ticket ticket3 = new Ticket("DME", "LED", 5000, 900, 1130);

        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);

        Ticket[] result = manager.search("DME", "LED");

        assertEquals(3, result.length);

        assertEquals(5000, result[0].getPrice());
        assertEquals(5000, result[1].getPrice());
        assertEquals(5000, result[2].getPrice());
    }
}