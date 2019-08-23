################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../SpeechFilterSpark.cpp \
../base64.cpp \
../call_stack_gcc.cpp \
../pugixml.cpp 

OBJS += \
./SpeechFilterSpark.o \
./base64.o \
./call_stack_gcc.o \
./pugixml.o 

CPP_DEPS += \
./SpeechFilterSpark.d \
./base64.d \
./call_stack_gcc.d \
./pugixml.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I../../Include -I"/adele/dev/workspace/EmailSenderSpark" -I../../include -I"/adele/dev/workspace/TRONHandlerSpark/include" -O0 -g3 -Wall -c -fmessage-length=0 -fPIC -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


