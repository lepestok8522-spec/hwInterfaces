import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AviaSoulsTest {

    @Test
    void testSearchSortByPrice() {
        AviaSouls manager = new AviaSouls();

        // Создаем билеты с разными ценами
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

        // Должно быть 4 билета на маршрут DME-LED
        assertEquals(4, result.length);

        // Проверяем сортировку по цене (от меньшей к большей)
        assertEquals(4500, result[0].getPrice()); // самый дешевый
        assertEquals(5000, result[1].getPrice());
        assertEquals(5500, result[2].getPrice());
        assertEquals(6000, result[3].getPrice()); // самый дорогой
    }

    @Test
    void testSearchNoResults() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);
        Ticket ticket2 = new Ticket("SVO", "KZN", 3000, 800, 1000);

        manager.add(ticket1);
        manager.add(ticket2);

        // Поиск по несуществующему маршруту
        Ticket[] result = manager.search("DME", "GOJ");
        assertEquals(0, result.length);

        // Поиск по существующему маршруту с 1 билетом
        Ticket[] result2 = manager.search("SVO", "KZN");
        assertEquals(1, result2.length);
        assertEquals(3000, result2[0].getPrice());
    }

    @Test
    void testTicketTimeComparator() {
        TicketTimeComparator comparator = new TicketTimeComparator();

        // Создаем билеты с разным временем полета
        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);  // полет 2 часа
        Ticket ticket2 = new Ticket("DME", "LED", 4500, 1400, 1530);  // полет 1.5 часа
        Ticket ticket3 = new Ticket("DME", "LED", 6000, 900, 1130);    // полет 2.5 часа
        Ticket ticket5 = new Ticket("DME", "LED", 5500, 1600, 1800);   // полет 2 часа

        // Проверяем сравнение по времени полета
        // ticket2: 1.5 часа, ticket1: 2 часа
        assertTrue(comparator.compare(ticket2, ticket1) < 0);

        // ticket1: 2 часа, ticket3: 2.5 часа
        assertTrue(comparator.compare(ticket1, ticket3) < 0);

        // ticket1 и ticket5: оба по 2 часа
        assertEquals(0, comparator.compare(ticket1, ticket5));
    }

    @Test
    void testSearchAndSortByTime() {
        AviaSouls manager = new AviaSouls();


        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);  // полет 2 часа
        Ticket ticket2 = new Ticket("DME", "LED", 4500, 1400, 1530);  // полет 1.5 часа
        Ticket ticket3 = new Ticket("DME", "LED", 6000, 900, 1130);    // полет 2.5 часа
        Ticket ticket4 = new Ticket("SVO", "KZN", 3000, 800, 1000);    // другой маршрут
        Ticket ticket5 = new Ticket("DME", "LED", 5500, 1600, 1800);   // полет 2 часа

        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);
        manager.add(ticket4);
        manager.add(ticket5);

        TicketTimeComparator comparator = new TicketTimeComparator();
        Ticket[] result = manager.searchAndSortBy("DME", "LED", comparator);

        assertEquals(4, result.length);

        assertEquals(ticket2, result[0]);


        boolean foundFirstTwoHour = false;
        boolean foundSecondTwoHour = false;

        if (result[1] == ticket1 || result[1] == ticket5) {
            foundFirstTwoHour = true;
        }
        if (result[2] == ticket1 || result[2] == ticket5) {
            foundSecondTwoHour = true;
        }

        assertTrue(foundFirstTwoHour && foundSecondTwoHour);

        assertEquals(ticket3, result[3]); // самый долгий полет
    }

    @Test
    void testSearchAndSortByTimeNoResults() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);
        manager.add(ticket1);

        TicketTimeComparator comparator = new TicketTimeComparator();
        Ticket[] result = manager.searchAndSortBy("DME", "GOJ", comparator);
        assertEquals(0, result.length);
    }

    @Test
    void testFindAll() {
        AviaSouls manager = new AviaSouls();

        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);
        Ticket ticket2 = new Ticket("SVO", "KZN", 3000, 800, 1000);

        manager.add(ticket1);
        manager.add(ticket2);

        Ticket[] allTickets = manager.findAll();
        assertEquals(2, allTickets.length);
    }

    @Test
    void testAddMultipleTickets() {
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
    void testSameFlightTimeDifferentPrices() {
        AviaSouls manager = new AviaSouls();


        Ticket ticket1 = new Ticket("DME", "LED", 5000, 1000, 1200);  // 2 часа
        Ticket ticket2 = new Ticket("DME", "LED", 4500, 1000, 1200);  // 2 часа
        Ticket ticket3 = new Ticket("DME", "LED", 6000, 1000, 1200);  // 2 часа

        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);


        TicketTimeComparator comparator = new TicketTimeComparator();
        Ticket[] result = manager.searchAndSortBy("DME", "LED", comparator);

        assertEquals(3, result.length);

        assertEquals(0, comparator.compare(result[0], result[1]));
        assertEquals(0, comparator.compare(result[1], result[2]));
    }
}