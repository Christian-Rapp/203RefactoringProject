
public class AllFalseEntityVisitor<R> implements EntityVisitor <R>{

	
	public Object visit(Ore ore) {
		return false;
	}

	
	public Object visit(Miner_Full mf) {
		return false;
	}

	
	public Object visit(Miner_Not_Full mnf) {
		return false;
	}

	
	public Object visit(Obstacle obst) {
		return false;
	}

	
	public Object visit(Ore_Blob oreblob) {
		return false;
	}

	
	public Object visit(Quake quake) {
		return false;
	}

	
	public Object visit(Vein vein) {
		return false;
	}

	
	public Object visit(Blacksmith blacksmith) {
		return false;
	}

}
