Given M memory blocks and two large relations R(X,Y) and S(Y,Z).Perform joins on two tables where Join condition  is (R.Y==S.Y).And None of the relation R or S fits completely in main memory.Also X,Y can be any number of attribute.


Perform join by:
	
	1.Sort-Merge join
	
	2.Hash-Merge join
	
Input Parameters:

	The files containing relations R and S and the value of M blocks.

	1.Attribute Type: Note that all attributes, X, Y and Z are strings and Y may be a non-key attribute.

	2.Block size: Assume that each block can store 100 tuples for both relations, R and S.

	3.Input Format: ./a.out <path of R file> <path of S file> <sort/hash> <M> Output:
	
Output:

	Output File:
		
		 Rfilename_Sfilename_join
