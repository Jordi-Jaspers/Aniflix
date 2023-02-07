import PocketBase from 'pocketbase';

export const pocketBase = new PocketBase(process.env.NEXT_PUBLIC_POCKETBASE_URL);

// globally disable auto cancellation
pocketBase.autoCancellation(false);
