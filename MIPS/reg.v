//reg.v
// Daniel Ortega [005326473] , Ben

`timescale 1ns / 1ps

module register(
    input	[4:0]	rs,
    input	[4:0]	rt,
    input	[4:0]	rd,
    input	[31:0]	writedata,
    input		 	regwrite,
    output reg	[31:0]	A,
    output reg	[31:0]	B
    );


reg [31:0] REG [0:31]; 
	
integer i;
	
initial 
begin
	    
	    A <= 0;
	    B <= 0;
		  
		  
		  for (i = 0; i < 32; i = i + 1)
			    REG[i] <= 0;
		#10;
     
     $display("From Register Memory:");
		 for (i = 0; i < 9; i = i + 1)
			 $display("\tREG[%0d] = %0d",i,REG[i]);
		
			  $display("\t...");
		  $display("\tREG[%0d] = %0d", 31, REG[31]);
end
	
always @ * 
begin
      A <= REG[rs]; // Assign the rs index of REG to A ;
	  B <= REG[rt]; // Assign the __ index of REG to B ;
		
	  	// WRITE data using index rd
		  if (regwrite)
			REG[rd] <= writedata;
	
end
endmodule 
