package abscon.instance.components;


public class PAllDifferent extends PGlobalConstraint {
	
	public PAllDifferent(String name, PVariable[] scope) {
		super(name, scope);
	}

	public long computeCostOf(int[] tuple) {
		for (int i = 0; i < tuple.length - 1; i++)
			for (int j = i + 1; j < tuple.length; j++)
				if (tuple[i] == tuple[j])
					return 1;
		return 0;
	}

	public String toString() {
		return super.toString() + " : allDifferent";
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
}
