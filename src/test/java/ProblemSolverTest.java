package test.java;

import main.java.ProblemSolver;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProblemSolverTest {

    private void checker(List<Integer> originalSet, int quantity, int expectedSum) {
        List<List<Integer>> subs = ProblemSolver.canBeDivided(originalSet, quantity);

        assertEquals(quantity, subs.size());

        subs.forEach(item -> {
            assertEquals(item.stream().mapToInt(i -> i).sum(), expectedSum);
        });
    }

    @Test
    void invalidParametersTest() {
        assertEquals(new ArrayList<>(), ProblemSolver.canBeDivided(Arrays.asList(1, 2, 3, 6), 0));
        assertEquals(new ArrayList<>(), ProblemSolver.canBeDivided(Arrays.asList(1, 2, 3, 6), 5));
        assertEquals(new ArrayList<>(), ProblemSolver.canBeDivided(Arrays.asList(1, 2, 3, 6), -1));
        assertEquals(new ArrayList<>(), ProblemSolver.canBeDivided(Collections.emptyList(), 3));
        assertEquals(new ArrayList<>(), ProblemSolver.canBeDivided(Arrays.asList(1, 2, 3, 6), 4));
    }

    @Test
    void validParametersTest() {
        checker(new ArrayList<>(Arrays.asList(1, 2, 3, 6)), 2, 6);
        checker(new ArrayList<>(Arrays.asList(1, 2, 5, 6)), 2, 7);
        checker(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)), 3, 5);
        checker(new ArrayList<>(Arrays.asList(1, 6, 4, 2, 5)), 3, 6);
        checker(new ArrayList<>(Arrays.asList(1, 2, 3, 6)), 1, 12);
        checker(new ArrayList<>(Arrays.asList(-1, 4, 2, 1)), 2, 3);
        checker(new ArrayList<>(Arrays.asList(-1, 5, 2, 4)), 2, 5);
        checker(new ArrayList<>(Arrays.asList(-8, -6, 1, 2, 3)), 2, -4);
        checker(new ArrayList<>(Arrays.asList(10, -5, 12, -7, 2, 3)), 3, 5);
    }

    @Test
    void validDuplicatedParametersTest() {
        checker(new ArrayList<>(Arrays.asList(1, 1, 1, 1)), 4, 1);
        checker(new ArrayList<>(Arrays.asList(1, 2, 1, 2, 3)), 3, 3);
        checker(new ArrayList<>(Arrays.asList(1, 1, 1, 3)), 2, 3);
        checker(new ArrayList<>(Arrays.asList(1, 2, 1, 2, 1, 2)), 3, 3);
        checker(new ArrayList<>(Arrays.asList(-1, -4, 2, 1, -4, -2, 8)), 3, 0);
    }
}