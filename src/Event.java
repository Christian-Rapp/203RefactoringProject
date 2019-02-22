final class Event
{
   public Action action;
   public long time;
   public Entity entity;

   public Event(Action actionInterface, long time, Entity entity2)
   {
      this.action = actionInterface;
      this.time = time;
      this.entity = entity2;
   }
}
