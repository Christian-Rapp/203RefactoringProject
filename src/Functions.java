import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;
import processing.core.PApplet;

final class Functions
{
   private final static Random rand = new Random();

   public static Random getRand() {
	return rand;
}
   public static Optional<Entity> nearestEntity(List<Entity> entities,
		      Point pos)
		   {
		      if (entities.isEmpty())
		      {
		         return Optional.empty();
		      }
		      else
		      {
		         Entity nearest = entities.get(0);
		         int nearestDistance = nearest.position.distanceSquared(null, pos);

		         for (Entity other : entities)
		         {
		            int otherDistance = other.position.distanceSquared(null, pos);

		            if (otherDistance < nearestDistance)
		            {
		               nearest = other;
		               nearestDistance = otherDistance;
		            }
		         }

		         return Optional.of(nearest);
		      }
		   }
	}



