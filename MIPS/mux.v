// mux.v
// Daniel Ortega [005326473] , Ben
`timescale 1ns / 1ps
module mux 
	#(parameter BITS = 32)(
	output	wire	[BITS-1:0]	y,
	input	wire	[BITS-1:0]	a,	b,		
	input	wire			sel	
   );
   
   assign	y = sel ? a : b;
	
endmodule 
