################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../SpeechFilterSpark.cpp \
../call_stack_gcc.cpp \
../pugixml.cpp 

OBJS += \
./SpeechFilterSpark.o \
./call_stack_gcc.o \
./pugixml.o 

CPP_DEPS += \
./SpeechFilterSpark.d \
./call_stack_gcc.d \
./pugixml.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I../../Include -I../../include -O3 -Wall -c -fmessage-length=0 -fPIC -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


