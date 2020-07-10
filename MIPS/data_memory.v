// data_memory.v
// Daniel Ortega [005326473] , Ben

`timescale 1ns / 1ps

module data_memory (
	input		wire	[31:0]	addr,			
	input		wire	[31:0]	write_data,		
	input		wire		memwrite,
	input		wire		memread,
	output		reg	[31:0]	read_data	
	);

   reg [31:0] DMEM[0:255];  

   integer i;
	
	initial begin
	
		read_data <= 0;
		
		$readmemb("data.txt",DMEM);
		
		for (i = 9; i < 256; i = i + 1)
			DMEM[i] = i;
	
		$display("From Data Memory (data.txt):");
		for (i = 0; i < 9; i = i + 1)
			$display("\tDMEM[%0d] = %0h", i, DMEM[i]);
		
		$display("From Data Memory (initial):");
		for (i = 9; i < 16; i = i + 1)
			$display("\tDMEM[%0d] = %0d", i, DMEM[i]);
		
		$display("\t...");
		$display("\tDMEM[%0d] = %0d", 255, DMEM[255]);
			
   end
   
    always@(addr or write_data or memwrite) begin
		if(memwrite == 1'b1)
			DMEM[addr] <= write_data;
	end

    always@(addr or memread) begin
		if(memread == 1'b1)
			read_data <= DMEM[addr];
	end	
	
endmodule 
