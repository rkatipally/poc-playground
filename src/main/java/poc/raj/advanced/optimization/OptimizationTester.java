package poc.raj.advanced.optimization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OptimizationTester {
    public final static String[] optimizationFields = new String[]{ "fareClasses", "footnote"};
    private static final String COMMA_DELIMITER = ",";

    public static void main(String[] args) {
        System.out.println("Hello World");
        List<Set<String>> allFilterCombinations = IntStream.range(1, optimizationFields.length + 1)
                .mapToObj(size -> Sets.combinations(Sets.newHashSet(optimizationFields), size))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<UIFareSearchCriteria> criteriaList = new ArrayList<>();
        criteriaList.add(UIFareSearchCriteria.builder().origin("WAS").destination("LON").carrier("AA").footnote("FN1").build());
        criteriaList.add(UIFareSearchCriteria.builder().origin("WAS").destination("LON").carrier("BA").footnote("FN2").build());

        criteriaList.add(UIFareSearchCriteria.builder().origin("LON").fareClasses("ABC,DEF").carrier("BA").footnote("DL").outboundDate(Optional.of(LocalDate.now())).build());

        optimizeRequests(criteriaList, allFilterCombinations);

    }

    public static void optimizeRequests(List<UIFareSearchCriteria> criteriaList, List<Set<String>> allFilterCombinations) {
        ObjectMapper oMapper = new ObjectMapper();
        List<Map<String, String>> criteriaMapList = oMapper.convertValue(criteriaList, new TypeReference<List<Map<String, String>>>() {
        });
        for(Set<String> filterSet: allFilterCombinations){
            criteriaMapList = criteriaMapList.stream()
                    .collect(Collectors.groupingBy(cr -> OptimizationTester.getGroupKey(cr, filterSet), LinkedHashMap::new, Collectors.toList()))
                    .values().stream()
                        .map(criteria -> combineFilters(criteria, filterSet))
                        .flatMap(Collection::stream).collect(Collectors.toList());
        }
        System.out.println(criteriaMapList);
    }

    public static List<Map<String, String>> combineFilters(List<Map<String, String>> criteriaMapList, Set<String> filtersToOptimize) {
        return criteriaMapList.stream().map(cr -> Arrays.asList(cr)).reduce(new ArrayList<>(), (cl1, cl2) -> {
            if (cl1.size() == 0) return cl2;
            if (cl2.size() == 0) return cl1;
            List<Map<String, String>> finalList = cl1.stream()
                    .map(cr1 -> cl2.stream()
                            .map(cr2 -> reduceFilters(cr1, cr2, new ArrayList<>(filtersToOptimize)))
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());
            return finalList;
        });
    }

    public static List<Map<String, String>> reduceFilters(Map<String, String> criteria1, Map<String, String> criteria2, List<String> filtersToOptimize) {
        List<String> allFilters1 = filtersToOptimize.stream().map(filter -> StringUtils.defaultIfEmpty(criteria1.get(filter), "")).collect(Collectors.toList());
        List<String> allFilters2 = filtersToOptimize.stream().map(filter -> StringUtils.defaultIfEmpty(criteria2.get(filter), "")).collect(Collectors.toList());
        List<Integer> subsets = Streams.zip(allFilters1.stream(), allFilters2.stream(), (filter1, filter2) -> {
            List<String> l1 = Arrays.asList(filter1);
            List<String> l2 = Arrays.asList(filter2);
            Collections.sort(l1);
            Collections.sort(l2);

            if (l1.equals(l2)) return 0;
            else if (Arrays.asList(filter1).containsAll(Arrays.asList(filter2))) return 1;
            else if (Arrays.asList(filter2).containsAll(Arrays.asList(filter1))) return 2;
            else return -1;
        }).collect(Collectors.toList());

        Map<Integer, Long> counts = subsets.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (counts.getOrDefault(-1, 0L) == subsets.size()) return Arrays.asList(criteria1, criteria2);
        else if (counts.getOrDefault(0, 0L) == subsets.size()) return Arrays.asList(criteria1);
        else if (counts.getOrDefault(1, 0L) == subsets.size() || counts.getOrDefault(0, 0L) + counts.getOrDefault(1, 0L) == subsets.size())
            return Arrays.asList(criteria1);
        else if (counts.getOrDefault(2, 0L) == subsets.size() || counts.getOrDefault(0, 0L) + counts.getOrDefault(2, 0L) == subsets.size())
            return Arrays.asList(criteria2);
        else if (counts.getOrDefault(-1, 0L) + counts.getOrDefault(0, 0L) == counts.size()) {
            criteria1.put(filtersToOptimize.get(subsets.indexOf(-1)),
                    String.join(COMMA_DELIMITER,
                            Sets.newHashSet((criteria1.get(filtersToOptimize.get(subsets.indexOf(-1)))
                                    + COMMA_DELIMITER + criteria2.get(filtersToOptimize.get(subsets.indexOf(-1)))).split(COMMA_DELIMITER))));
            return Arrays.asList(criteria1);
        } else return Arrays.asList(criteria1, criteria2);

    }

    public static String getGroupKey(Map<String, String> criteria, Set<String> excludeSet) {
        return Arrays.asList(optimizationFields).stream()
                .filter(f -> !excludeSet.contains(f))
                .map(f -> criteria.get(f))
                .filter(s -> !Objects.isNull(s))
                .collect(Collectors.joining());
    }
}
