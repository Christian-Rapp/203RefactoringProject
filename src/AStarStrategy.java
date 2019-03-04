import java.util.Comparator;
import java.util.List;
import java.util.function.*;
import java.util.stream.*;

public class AStarStrategy implements PathingStrategy {

	
	public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
			BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
		
		Function<Point, Integer> heuristic = current -> current.distanceSquared(end);
		
		Comparator<Point> compareTo = (p1, p2) -> heuristic.apply(p1).compareTo(heuristic.apply(p2));
		
		return potentialNeighbors.apply(start)
			.filter(canPassThrough)
			.filter(pt -> !pt.equals(start) && !pt.equals(end))
			.sorted(compareTo)
			.limit(1)
			.collect(Collectors.toList());
		
		}

}
