// ex_mem.v
// Daniel Ortega [005326473] , Ben

`timescale 1ns / 1ps

module ex_mem(
	input	wire	[1:0]		ctlwb_out,
	input	wire	[2:0]		ctlm_out,
	input	wire	[31:0]		adder_out,
	input	wire				aluzero,
	input	wire	[31:0]		readdat2,
	input	wire	[31:0]		aluout,
	input	wire	[4:0]		muxout,
	output	reg		[1:0]		wb_ctlout,
	output	reg					branch,
	output	reg					memread,
	output	reg					memwrite,
	output	reg		[31:0]		add_result,
	output	reg					zero,
	output	reg		[31:0]		alu_result,
	output	reg		[31:0]		rdata2out,
	output	reg		[4:0]		five_bit_muxout
    );

	initial begin
		wb_ctlout 		<= 0; 
		branch 			<= 0; 
		memread 		<= 0;
		memwrite 		<= 0;
		add_result 		<= 0;
		zero 			<= 0;
		alu_result 		<= 0;
		rdata2out 		<= 0;
		five_bit_muxout <= 0;
	end

	always@* begin
		#1 
		wb_ctlout 		<= ctlwb_out;
		branch 			<= ctlm_out[2];    
		memread 		<= ctlm_out[1];
		memwrite 		<= ctlm_out[0];    
        add_result 		<= adder_out;   
		zero 			<= aluzero;
		alu_result 		<= aluout;
		rdata2out 		<= readdat2;
		five_bit_muxout <= muxout;
	end

endmodule 
