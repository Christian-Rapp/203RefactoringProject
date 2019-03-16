
public class ActionableVisitor extends AllFalseEntityVisitor{
	
	public Boolean visit(Ore ore) {
		return true;
	}
	
	public Boolean visit(Miner_Full mf) {
		return true;
	}

	
	public Boolean visit(Miner_Not_Full mnf) {
		return true;
	}
	
	public Boolean visit(Ore_Blob oreblob) {
		return true;
	}
	
	public Boolean visit(Quake quake) {
		return true;
	}
	
	public Boolean visit(Vein vein) {
		return true;
	}
	
	public Boolean visit(DevitoShrine shrine)
	{
		return true;
	}
	
	public Boolean visit(Turtle turtle)
	{
		return true;
	}
	

}
