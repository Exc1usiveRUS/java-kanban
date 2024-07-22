package managerTests;

import org.junit.jupiter.api.Test;

import taskManager.Managers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    void shouldNotNull() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void shouldNotNullHistory() {
        assertNotNull(Managers.getDefaultHistory());
    }

}