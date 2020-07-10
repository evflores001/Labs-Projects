// mem.v
// Daniel Ortega [005326473] , Ben

`timescale 1ns / 1ps

module memory (
   output reg [31:0] data,       // Output of Instruction Memory
   input wire [31:0] addr        // Input of Instruction Memory
   );

	parameter NOP		= 32'b1000_0000_0000_0000_0000_0000_0000_0000;	
	integer i;

   
   reg [31:0] MEM[0:127];  

   	initial begin

		data <= 0;
		
		$readmemb("risc.txt",MEM);
		for (i = 37; i < 127; i = i + 1)
			MEM[i] = NOP;
		#0	
		for (i = 0; i < 24; i = i + 1)
			$display(MEM[i]);
		
	end

   
   always @ (addr) data <= MEM[addr];
endmodule
