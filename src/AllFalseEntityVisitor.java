
public class AllFalseEntityVisitor implements EntityVisitor<Boolean>{

	
	public Boolean visit(Ore ore) {
		return false;
	}

	
	public Boolean visit(Miner_Full mf) {
		return false;
	}

	
	public Boolean visit(Miner_Not_Full mnf) {
		return false;
	}

	
	public Boolean visit(Obstacle obst) {
		return false;
	}

	
	public Boolean visit(Ore_Blob oreblob) {
		return false;
	}

	
	public Boolean visit(Quake quake) {
		return false;
	}

	
	public Boolean visit(Vein vein) {
		return false;
	}

	
	public Boolean visit(Blacksmith blacksmith) {
		return false;
	}
	
	public Boolean visit(DevitoShrine shrine) {
		return false;
	}
	
	public Boolean visit(Monk monk) {
		return false;
	}


	public Boolean visit(Turtle turtle) {
		return false;
	}
}
