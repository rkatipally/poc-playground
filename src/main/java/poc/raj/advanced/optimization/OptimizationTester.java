package poc.raj.advanced.optimization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OptimizationTester {
    public final static String[] optimizationFields = new String[]{"origin", "destination", "carrier", "fareClasses", "footnote"};

    public static void main(String[] args) {
        System.out.println("Hello World");
        IntStream.range(1, optimizationFields.length + 1)
                .mapToObj(size -> Sets.combinations(Sets.newHashSet(optimizationFields), size))
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .forEach(size -> System.out.println(size));
//    Set<Set<String>> combinations = Sets.combinations(Sets.newHashSet(optimizationFields), 3);
//        combinations.forEach(System.out::println);
    //        System.out.println(combinations);
    List<UIFareSearchCriteria> criteriaList = new ArrayList<>();
        criteriaList.add(UIFareSearchCriteria.builder().

    origin("WAS").

    build());
        criteriaList.add(UIFareSearchCriteria.builder().

    origin("LON").

    fareClasses("ABC,DEF").

    outboundDate(Optional.of(LocalDate.now())).

    build());

    optimizeRequests(criteriaList);

}

    public static void optimizeRequests(List<UIFareSearchCriteria> criteriaList) {
        ObjectMapper oMapper = new ObjectMapper();
        List<Map<String, String>> criteriaMapList = oMapper.convertValue(criteriaList, new TypeReference<List<Map<String, String>>>() {
        });
        System.out.println(criteriaMapList);
//        criteriaMapList.stream();
    }


    public static List<int[]> generate(int n, int r) {
        List<int[]> combinations = new ArrayList<>();
        helper(combinations, new int[r], 0, n - 1, 0);
        return combinations;
    }

    private static void helper(List<int[]> combinations, int data[], int start, int end, int index) {
        if (index == data.length) {
            int[] combination = data.clone();
            combinations.add(combination);
        } else if (start <= end) {
            data[index] = start;
            helper(combinations, data, start + 1, end, index + 1);
            helper(combinations, data, start + 1, end, index);
        }
    }
}
