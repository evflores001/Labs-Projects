//MEMORY.v
// Daniel Ortega [005326473] , Ben
`timescale 1ns / 1ps

module MEMORY(
	input		wire	[1:0]		wb_ctlout,
	input		wire				branch,
	input		wire				memread,
	input		wire				memwrite,
	input		wire				zero,
	input		wire	[31:0]		alu_result,
	input		wire	[31:0]		rdata2out,
	input		wire	[4:0]		five_bit_muxout,
	output	wire				MEM_PCSrc,
	output	wire				MEM_WB_regwrite,
	output	wire				MEM_WB_memtoreg,
	output	wire	[31:0]		read_data,
	output	wire	[31:0]		mem_alu_result,
	output	wire	[4:0]		mem_write_reg
    );

	
	wire	[31:0]	read_data_in;
	

		AND AND_4	(
		.membranch(branch),	
		.zero(zero),
		.PCSrc(MEM_PCSrc));
					
	data_memory data_memory4	(
		.addr(alu_result),		//	input		wire	[31:0]
		.write_data(rdata2out),         //	input		wire	[31:0]
		.memwrite(memwrite),            //	input		wire			
		.memread(memread),              //	input		wire			
		.read_data(read_data_in));      //	output		reg	[31:0]
										
	mem_wb mem_wb4	(
		.control_wb_in(wb_ctlout),	//	input	wire	[1:0]	
		.read_data_in(read_data_in),    //	input	wire	[31:0]	
		.alu_result_in(alu_result),     //	input	wire	[31:0]	
		.write_reg_in(five_bit_muxout), //	input	wire	[4:0]	
		.regwrite(MEM_WB_regwrite),     //	output	reg				
		.memtoreg(MEM_WB_memtoreg),     //	output	reg				
		.read_data(read_data),          //	output	reg	[31:0]	
		.mem_alu_result(mem_alu_result),//	output	reg	[31:0]	
		.mem_write_reg(mem_write_reg)); //	output	reg	[4:0]	
	
endmodule  
