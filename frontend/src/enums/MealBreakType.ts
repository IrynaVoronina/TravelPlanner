export enum MealBreakType {
    BREAKFAST = 'BREAKFAST',
    LUNCH = 'LUNCH',
    DINNER = 'DINNER'
}

export const MealBreakTypeLabels: Record<MealBreakType, string> = {
    [MealBreakType.BREAKFAST]: 'Сніданок',
    [MealBreakType.LUNCH]: 'Обід',
    [MealBreakType.DINNER]: 'Вечеря'
};
