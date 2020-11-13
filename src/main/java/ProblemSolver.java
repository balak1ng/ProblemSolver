package main.java;

import java.util.*;

//ТЗ. Распределить заданное количество целых чисел (возможны повторяющиеся числа) на заданное.
//число групп так, чтобы были равными суммы чисел, входящих в каждую группу. Если это сделать
//невозможно, то программа определяет этот факт.

//Реализовано для повторяющихся и целых чисел (отрицательных в том числе).

//Основная функция canBeDivided принимает на вход список и число необходимых разбиений.
//Некорректными стартовыми данными будут являтся пустой список, число разбиений меньше либо равное нуля и число
//разбиений больших размера исходного списка. В таком случае на выходе/ответом будет пустой список.
//Особым случаем будет являтся, когда число разбиений равно 1. В таком случае ответом будет само множество.
//Также, если число разбиений равняется размеру исходного списка, то необходимо убедиться, что все числа в списке равны,
//чтобы существовало единственное решение.
//При валидных стартовых данных и при наличии решения на выходе функции список подсписков, из которых
//составлен оригинальный список. Число элементов в ответе будет равнятся указанному числу разбиений.
//Если данные валидны, но решения нет, функция вернет пустой список.
//
//Схема реализации: находим множество всех подмножеств исходного множества. Строим карту, по такому принципу:
//ключ - сумма чисел подмножества, значение - множество подмножеств, которые дают данную сумму.
//Имея заданное число разбиений, следим за количеством включений по каждому ключу, и как только число включений
//больше либо равно числу разбиений, заходим в данное множество и пытаемся при помощи его элементов
//получить исходное (убираем его из исходного, смотрим, остались ли пересечения c другими).

public class ProblemSolver {

    //Функция getSubsets позволяет при помощи битового сдвига найти множество всех подмножеств исходного множества
    //Т.е. иными словами идет от элемента 00..0 до 11..1 по битам и либо включает, либо не включает элемент исходного множества
    private static List<List<Integer>> getSubsets(List<Integer> set) {

        List<List<Integer>> subsets = new ArrayList<>();

        int max = 1 << set.size();

        for (int i = 0; i < max; i++) {
            List<Integer> subset = new ArrayList<>();
            for (int j = 0; j < set.size(); j++) {
                if (((i >> j) & 1) == 1) {
                    subset.add(set.get(j));
                }
            }
            //Пустое множество не добавляем, оно нам не нужно
            if (!subset.isEmpty()) {
                subsets.add(subset);
            }
        }
        return subsets;
    }


    //Основная функция, которая находит разбиение исходного множества за указанное число шагов, либо вовзвращает пустой список
    public static List<List<Integer>> canBeDivided(List<Integer> originalList, int N) {

        List<List<Integer>> answerList = new ArrayList<>();

        if (N <= 0 || N > originalList.size()) {
            return answerList;
        }

        if (N == 1) {
            answerList.add(originalList);
            return answerList;
        }

        //Особый случай, если число разбиений равняется числу элементов
        if (N == originalList.size()) {
            for (Integer x : originalList) {
                if (!x.equals(originalList.get(0))) {
                    return answerList;
                }
            }
        }

        List<List<Integer>> subsetsOriginalSet = getSubsets(originalList);

        Map<Integer, List<List<Integer>>> myHashMap = new HashMap<>();

        //заполнение Мапы следующее: каждой возможной сумме среди всех подмножеств сопоставляется
        //множество подмножеств, которые дают данную сумму
        for (List<Integer> subset : subsetsOriginalSet) {
            Integer sum = 0;
            List<List<Integer>> valueList = new ArrayList<>();
            for (Integer x : subset) {
                sum += x;
            }
            if (myHashMap.containsKey(sum)) {
                valueList = myHashMap.get(sum);
            }
            valueList.add(subset);
            myHashMap.put(sum, valueList);
        }

        //О том, возможно ли в теории разбиение, нам говорит количество подмножеств, которые дают сумму
        //Если их число равно или больше заданного, то разбиение возможно существует
        //Иные случаи не рассматриваются, т.к. иначе разбиения априори нет (сумма не повтояется N раз)
        for (List<List<Integer>> setOfSubsets : myHashMap.values()) {
            int index = 0;
            ArrayList<Integer> copyOfOriginalList = new ArrayList<>(originalList);
            if (setOfSubsets.size() >= N) {

                for (List<Integer> subset : setOfSubsets) {

                    if (setFullyContains(copyOfOriginalList, subset)) {
                        for (Integer x : subset) {
                            copyOfOriginalList.remove(x);
                        }

                        if (copyOfOriginalList.isEmpty()) {
                            if (index + 1 == N) {
                                answerList.add(subset);
                            } else {
                                answerList.clear();
                            }
                            return answerList;
                        }

                        //следим за тем, остались ли хоть какие-то пересечения при удалении
                        int counter = 0;
                        for (List<Integer> otherSubsets : setOfSubsets) {
                            if (setFullyContains(copyOfOriginalList, otherSubsets)) {
                                counter++;
                            }
                        }
                        if (counter == 0) {
                            copyOfOriginalList.addAll(subset);
                            answerList.remove(subset);
                        } else {
                            answerList.add(subset);
                            index++;
                        }
                    }
                }
                answerList.clear();
            }
        }
        return answerList;
    }

    //Данная функция нужна, потому что в оригинальном массиве могут быть повторяющиеся числа
    //Библиотечная функция contains не обращает внимание на частоту встречания числа
    private static boolean setFullyContains(List<Integer> originalSet, List<Integer> otherList) {
        for (Integer x : otherList) {
            if (Collections.frequency(otherList, x) > Collections.frequency(originalSet, x)) {
                return false;
            }
        }
        return true;
    }
}