// s_extend.v
// Daniel Ortega [005326473] , Ben

`timescale 1ns / 1ps


module s_extend(
    input	wire	[15:0] nextend,
    output	reg		[31:0] extend
);

	always@ * 
	begin
		
		extend <= {{16{nextend[15]}}, nextend};
	end

endmodule
