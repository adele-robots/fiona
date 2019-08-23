################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../ZendeskSpark.cpp 

OBJS += \
./ZendeskSpark.o 

CPP_DEPS += \
./ZendeskSpark.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I"/adele/dev/workspace/include" -I"/adele/dev/workspace/Dependencies/boost_1_52_0" -I"/adele/dev/workspace/Dependencies/libconfig-1.4.8/lib" -I"/adele/dev/workspace/Include" -include"/adele/dev/workspace/include/base64.h" -O0 -g3 -Wall -c -fmessage-length=0 -fPIC -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


