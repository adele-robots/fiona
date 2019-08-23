################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../TicketConnectorSpark.cpp 

OBJS += \
./TicketConnectorSpark.o 

CPP_DEPS += \
./TicketConnectorSpark.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I/usr/include/ -I"/adele/dev/workspace/include" -I"/adele/dev/workspace/Include/jsoncpp" -I"/adele/dev/workspace/Include" -O0 -g3 -pedantic -Wall -c -fmessage-length=0 -std=c++0x -fPIC -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


