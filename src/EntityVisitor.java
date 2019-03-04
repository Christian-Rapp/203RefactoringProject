
public interface EntityVisitor<R> {

	R visit(Ore ore);
	R visit(Miner_Full mf);
	R visit(Miner_Not_Full mnf);
	R visit(Obstacle obst);
	R visit(Ore_Blob oreblob);
	R visit(Quake quake);
	R visit(Vein vein);
	R visit(Blacksmith blacksmith);
	
}
