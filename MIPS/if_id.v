// if_id.v
// Daniel Ortega [005326473] , Ben

`timescale 1ns / 1ps

module if_id (
   output reg [31:0] instrout,       // Output of IF/ID Instruction Register
                       npcout,       // Output of IF/ID NPC Register
   input wire [31:0]    instr,       // Input of IF/ID Instruction Register
                          npc        // Input of IF/ID NPC Register
   );
   initial begin
      instrout <= 0;
      npcout   <= 0;
   end
   always @* begin
      #1 instrout <= instr;
           npcout <= npc;
   end
endmodule 
