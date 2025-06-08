export enum TravelMode {
    WALK = 'WALK',
    CAR = 'CAR',
    TRANSIT = 'TRANSIT'
}

export const TravelModeLabels: Record<TravelMode, string> = {
    [TravelMode.WALK]: 'Пішки',
    [TravelMode.CAR]: 'Автомобіль',
    [TravelMode.TRANSIT]: 'Громадський транспорт'
};