final class Event
{
   public ActionInterface action;
   public long time;
   public EntityInterface entity;

   public Event(ActionInterface actionInterface, long time, EntityInterface entity2)
   {
      this.action = actionInterface;
      this.time = time;
      this.entity = entity2;
   }
}
