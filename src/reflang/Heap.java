package reflang;

/**
 * Representation of a heap, which maps references to values.
 * 
 * @author hridesh
 *
 */
public interface Heap {

	Value.RefVal ref (Value value) throws HeapException;

	Value deref (Value.RefVal loc) throws HeapException;

	Value setref (Value.RefVal loc, Value value) throws HeapException;

	Value.RefVal free (Value.RefVal value) throws HeapException;

	@SuppressWarnings("serial")
	static public class HeapException extends RuntimeException {
		HeapException(String message){
			super(message);
		}
	}

	static public class Heap16Bit implements Heap {
		static final int HEAP_SIZE = 65_536;
		
		Value[] _rep = new Value[HEAP_SIZE];
		int index = 0;
		
		public Value.RefVal ref (Value value) {
			if(index >= HEAP_SIZE)
				throw new HeapException("Fatal Error: Allowed memory size of " + Integer.MAX_VALUE + "exhausted.");
			Value.RefVal new_loc = new Value.RefVal(index);
			_rep[index] = value;
			index++;
			return new_loc;
		}

		public Value deref (Value.RefVal loc) {
			try {
				return _rep[loc.loc()];
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new HeapException("Fatal Error: Segmentation fault at memory access " + loc);
			}
		}

		public Value setref (Value.RefVal loc, Value value) {
			try {
				return _rep[loc.loc()] = value;
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new HeapException("Fatal Error: Segmentation fault at memory access " + loc);
			}
		}

		public Value.RefVal free (Value.RefVal loc) {
			try {
				_rep[loc.loc()] = null;
				//REMARK: students should add this location to free list.
				return loc;
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new HeapException("Fatal Error: Segmentation fault at memory access " + loc);
			}
		}

		public Heap16Bit(){}
	}

}
