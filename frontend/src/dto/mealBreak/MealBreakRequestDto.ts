import type {MealBreakType} from "../../enums/MealBreakType.ts";

export interface MealBreakRequestDto {
    name: MealBreakType;
    description: string;
    startTime: string;
    endTime: string;
}